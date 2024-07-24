package com.example.e_commerce.shopping_feature.data.repository

import com.example.e_commerce.core.utils.CrashlyticsUtils
import com.example.e_commerce.core.utils.Resource
import com.example.e_commerce.core.utils.SpecialSectionsException
import com.example.e_commerce.shopping_feature.data.models.CategoryModel
import com.example.e_commerce.shopping_feature.data.models.ProductModel
import com.example.e_commerce.shopping_feature.data.models.SpecialSectionModel
import com.example.e_commerce.shopping_feature.data.models.SpecialSections
import com.example.e_commerce.shopping_feature.domain.models.toUIModel
import com.example.e_commerce.shopping_feature.domain.repository.ProductsRepository
import com.example.e_commerce.shopping_feature.presentation.models.CategoryUIModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
) :ProductsRepository {
    override fun getCategories(): Flow<Resource<List<CategoryUIModel>>> {
        return flow {
            try {
                emit(Resource.Loading())
                val categories = fireStore.collection("categories").get().await()
                    .toObjects(CategoryModel::class.java)

                emit(Resource.Success(categories.map { it.toUIModel() }))
            } catch (e: Exception) {
                emit(Resource.Error(e))
            }
        }
    }

    override fun getSaleProducts(
        countryID: String, saleType: String, pageLimit: Int
    ): Flow<List<ProductModel>> {
        return flow {
            val products = fireStore.collection("products").whereEqualTo("sale_type", saleType)
                .whereEqualTo("country_id", countryID).orderBy("price").limit(pageLimit.toLong())
                .get().await().toObjects(ProductModel::class.java)
            emit(products)
        }
    }

    override fun recommendProductsSection() = flow {
        try {
            val specialSection = fireStore.collection("special_sections")
                .document(SpecialSections.RECOMMENDED_PRODUCTS.id).get().await()
                .toObject(SpecialSectionModel::class.java)

            emit(specialSection)
        } catch (e: Exception) {
            val msg = e.message ?: "Error fetching recommended products"
            CrashlyticsUtils.sendCustomLogToCrashlytics<SpecialSectionsException>(
                msg, Pair(CrashlyticsUtils.SPECIAL_SECTIONS, msg)
            )
            emit(null)
        }

    }

    override suspend fun getAllProductsPaging(
        countryID: String, pageLimit: Long, lastDocument: DocumentSnapshot?
    ) = flow<Resource<QuerySnapshot>> {
        try {
            emit(Resource.Loading())

            var firstQuery = fireStore.collection("products").orderBy("price")

            if (lastDocument != null) {
                firstQuery = firstQuery.startAfter(lastDocument)
            }

            firstQuery = firstQuery.limit(pageLimit)

            val products = firstQuery.get().await()
            emit(Resource.Success(products))
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }

    override fun listenToProductDetails(productID: String): Flow<ProductModel> {
        return callbackFlow {
            val listener = fireStore.collection("products").document(productID)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }

                    val product = value?.toObject(ProductModel::class.java)
                    if (product != null) {
                        trySend(product)
                    } else {
                        close()
                    }
                }

            awaitClose { listener.remove() }
        }
    }
}