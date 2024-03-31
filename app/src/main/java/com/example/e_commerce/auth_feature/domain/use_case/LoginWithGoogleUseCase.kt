package com.example.e_commerce.auth_feature.domain.use_case

import com.example.e_commerce.auth_feature.domain.repository.FirebaseAuthRepository
import com.example.e_commerce.core.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginWithGoogleUseCase @Inject constructor(private val firebaseAuthRepository: FirebaseAuthRepository) {

    suspend operator fun invoke(idToken: String): Flow<Resource<String>> {
        return firebaseAuthRepository.loginWithGoogle(idToken)
    }
}