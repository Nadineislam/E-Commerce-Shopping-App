package com.example.e_commerce.shopping_feature.data.repository

import com.example.e_commerce.core.utils.Resource
import com.example.e_commerce.shopping_feature.data.models.CategoryModel
import com.example.e_commerce.shopping_feature.data.models.ProductModel
import com.example.e_commerce.shopping_feature.domain.models.toUIModel
import com.example.e_commerce.shopping_feature.domain.repository.CategoriesRepository
import com.example.e_commerce.shopping_feature.presentation.models.CategoryUIModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CategoriesRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
) :CategoriesRepository{
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

}