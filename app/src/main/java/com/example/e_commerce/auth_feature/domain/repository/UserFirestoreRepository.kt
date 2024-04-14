package com.example.e_commerce.auth_feature.domain.repository

import com.example.e_commerce.auth_feature.data.models.UserDetailsModel
import com.example.e_commerce.core.utils.Resource
import kotlinx.coroutines.flow.Flow

interface UserFirestoreRepository {
    suspend fun getUserDetails(userId: String): Flow<Resource<UserDetailsModel>>

}