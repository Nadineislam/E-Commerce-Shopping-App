package com.example.e_commerce.auth_feature.domain.repository

import kotlinx.coroutines.flow.Flow

interface AppDataStoreRepository {
    suspend fun saveLoginState(isLoggedIn: Boolean)
    suspend fun isLoggedIn(): Flow<Boolean>
}