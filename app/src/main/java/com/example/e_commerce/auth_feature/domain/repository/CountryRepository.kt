package com.example.e_commerce.auth_feature.domain.repository

import com.example.e_commerce.auth_feature.data.models.CountryModel
import kotlinx.coroutines.flow.Flow

interface CountryRepository {
    fun getCountries(): Flow<List<CountryModel>>
}