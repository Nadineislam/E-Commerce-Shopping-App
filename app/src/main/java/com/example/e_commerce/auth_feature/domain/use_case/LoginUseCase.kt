package com.example.e_commerce.auth_feature.domain.use_case

import com.example.e_commerce.auth_feature.data.models.UserDetailsModel
import com.example.e_commerce.auth_feature.domain.repository.FirebaseAuthRepository
import com.example.e_commerce.core.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val firebaseAuthRepository: FirebaseAuthRepository) {
    suspend operator fun invoke(email: String, password: String): Flow<Resource<UserDetailsModel>> =
        firebaseAuthRepository.loginWithEmailAndPassword(email, password)

}