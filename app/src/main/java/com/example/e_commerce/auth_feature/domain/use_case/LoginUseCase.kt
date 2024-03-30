package com.example.e_commerce.auth_feature.domain.use_case

import com.example.e_commerce.auth_feature.domain.repository.FirebaseAuthRepository
import com.example.e_commerce.core.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val firebaseAuthRepository: FirebaseAuthRepository) {
    suspend operator fun invoke(email: String, password: String): Flow<Resource<String>> =
        firebaseAuthRepository.loginWithEmailAndPassword(email, password)

}