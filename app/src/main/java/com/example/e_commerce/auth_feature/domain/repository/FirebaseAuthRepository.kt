package com.example.e_commerce.auth_feature.domain.repository

import com.example.e_commerce.auth_feature.data.models.UserDetailsModel
import com.example.e_commerce.core.utils.Resource
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthRepository {
    suspend fun loginWithEmailAndPassword(
        email: String, password: String
    ): Flow<Resource<UserDetailsModel>>

    suspend fun loginWithGoogle(
        idToken: String
    ): Flow<Resource<UserDetailsModel>>
    suspend fun loginWithFacebook(token: String): Flow<Resource<UserDetailsModel>>

    fun logout()
}