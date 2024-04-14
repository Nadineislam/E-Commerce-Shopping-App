package com.example.e_commerce.auth_feature.domain.repository

import com.example.e_commerce.auth_feature.data.user.UserDetailsPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    fun getUserDetails(): Flow<UserDetailsPreferences>
    suspend fun updateUserId(userId: String)
    suspend fun getUserId(): Flow<String>
    suspend fun clearUserPreferences()
    suspend fun updateUserDetails(userDetailsPreferences: UserDetailsPreferences)
}