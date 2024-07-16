package com.example.e_commerce.auth_feature.domain.repository

import com.example.e_commerce.auth_feature.data.user.CountryData
import com.example.e_commerce.auth_feature.data.user.UserDetailsPreferences
import com.example.e_commerce.auth_feature.presentation.model.CountryUIModel
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    fun getUserDetails(): Flow<UserDetailsPreferences>
    suspend fun updateUserId(userId: String)
    suspend fun getUserId(): Flow<String>
    suspend fun clearUserPreferences()
    suspend fun updateUserDetails(userDetailsPreferences: UserDetailsPreferences)
    suspend fun saveUserCountry(countryId: CountryUIModel)
    fun getUserCountry():  Flow<CountryData>
}