package com.example.e_commerce.shopping_feature.domain.repository

import com.example.e_commerce.core.utils.Resource
import com.example.e_commerce.shopping_feature.presentation.models.SalesAdUIModel
import kotlinx.coroutines.flow.Flow

interface SalesAdRepository {
    fun getSalesAds(): Flow<Resource<List<SalesAdUIModel>>>

}