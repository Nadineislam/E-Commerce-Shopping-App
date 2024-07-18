package com.example.e_commerce.shopping_feature.domain.repository

import com.example.e_commerce.core.utils.Resource
import com.example.e_commerce.shopping_feature.data.models.ProductModel
import com.example.e_commerce.shopping_feature.presentation.models.CategoryUIModel
import kotlinx.coroutines.flow.Flow

interface CategoriesRepository {
    fun getCategories(): Flow<Resource<List<CategoryUIModel>>>

    fun getSaleProducts(
        countryID: String, saleType: String, pageLimit: Int
    ): Flow<List<ProductModel>>
}