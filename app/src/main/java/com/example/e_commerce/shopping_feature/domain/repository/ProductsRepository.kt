package com.example.e_commerce.shopping_feature.domain.repository

import com.example.e_commerce.core.utils.Resource
import com.example.e_commerce.shopping_feature.data.models.ProductModel
import com.example.e_commerce.shopping_feature.data.models.SpecialSectionModel
import com.example.e_commerce.shopping_feature.presentation.models.CategoryUIModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    fun getCategories(): Flow<Resource<List<CategoryUIModel>>>

    fun getSaleProducts(
        countryID: String, saleType: String, pageLimit: Int
    ): Flow<List<ProductModel>>

    fun recommendProductsSection(): Flow<SpecialSectionModel?>

    suspend fun getAllProductsPaging(
        countryID: String, pageLimit: Long, lastDocument: DocumentSnapshot? = null
    ): Flow<Resource<QuerySnapshot>>

    fun listenToProductDetails(productID: String): Flow<ProductModel>
}