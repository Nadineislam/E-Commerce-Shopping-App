package com.example.e_commerce.auth_feature.data.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferenceRepository {
    suspend fun saveLoginState(isLoggedIn: Boolean)
    suspend fun saveUserID(userId: String)
    suspend fun isUserLoggedIn(): Flow<Boolean>
    fun getUserID(): Flow<String?>
}