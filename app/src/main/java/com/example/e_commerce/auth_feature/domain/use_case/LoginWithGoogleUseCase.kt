package com.example.e_commerce.auth_feature.domain.use_case

import com.example.e_commerce.auth_feature.data.models.UserDetailsModel
import com.example.e_commerce.auth_feature.domain.repository.FirebaseAuthRepository
import com.example.e_commerce.core.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginWithGoogleUseCase @Inject constructor(private val firebaseAuthRepository: FirebaseAuthRepository) {

    suspend operator fun invoke(idToken: String): Flow<Resource<UserDetailsModel>> {
        return firebaseAuthRepository.loginWithGoogle(idToken)
    }
}