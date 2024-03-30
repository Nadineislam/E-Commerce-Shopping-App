package com.example.e_commerce.auth_feature.domain.repository

import com.example.e_commerce.core.utils.Resource
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthRepository {
    suspend fun loginWithEmailAndPassword(
        email: String, password: String
    ): Flow<Resource<String>>
}