package com.example.e_commerce.auth_feature.data.repository

import com.example.e_commerce.auth_feature.data.models.AuthProvider
import com.example.e_commerce.auth_feature.data.models.UserDetailsModel
import com.example.e_commerce.auth_feature.domain.repository.FirebaseAuthRepository
import com.example.e_commerce.core.utils.CrashlyticsUtils
import com.example.e_commerce.core.utils.LoginException
import com.example.e_commerce.core.utils.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : FirebaseAuthRepository {
    override suspend fun loginWithEmailAndPassword(
        email: String, password: String
    ) = login(AuthProvider.EMAIL) { auth.signInWithEmailAndPassword(email, password).await() }

    override suspend fun loginWithGoogle(idToken: String) = login(AuthProvider.GOOGLE) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).await()
    }

    override suspend fun loginWithFacebook(token: String) = login(AuthProvider.FACEBOOK) {
        val credential = FacebookAuthProvider.getCredential(token)
        auth.signInWithCredential(credential).await()
    }

    private suspend fun login(
        provider: AuthProvider,
        signInRequest: suspend () -> AuthResult,
    ): Flow<Resource<UserDetailsModel>> = flow {
        try {
            emit(Resource.Loading())
            val authResult = signInRequest()
            val userId = authResult.user?.uid

            if (userId == null) {
                val msg = "Sign in UserID not found"
                logAuthIssueToCrashlytics(msg, provider.name)
                emit(Resource.Error(Exception(msg)))
                return@flow
            }

            val userDoc = firestore.collection("users").document(userId).get().await()
            if (!userDoc.exists()) {
                val msg = "Logged in user not found in firestore"
                logAuthIssueToCrashlytics(msg, provider.name)
                emit(Resource.Error(Exception(msg)))
                return@flow
            }

            val userDetails = userDoc.toObject(UserDetailsModel::class.java)
            userDetails?.let {
                emit(Resource.Success(userDetails))
            } ?: run {
                val msg = "Error mapping user details to UserDetailsModel, user id = $userId"
                logAuthIssueToCrashlytics(msg, provider.name)
                emit(Resource.Error(Exception(msg)))
            }
        } catch (e: Exception) {
            logAuthIssueToCrashlytics(
                e.message ?: "Unknown error from exception = ${e::class.java}", provider.name
            )
            emit(Resource.Error(e))
        }
    }

    override suspend fun registerWithEmailAndPassword(
        name: String,
        email: String,
        password: String
    ): Flow<Resource<UserDetailsModel>> {
        return flow {
            try {
                emit(Resource.Loading())
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val userId = authResult.user?.uid

                if (userId == null) {
                    val msg = "Sign up UserID not found"
                    logAuthIssueToCrashlytics(msg, AuthProvider.EMAIL.name)
                    emit(Resource.Error(Exception(msg)))
                    return@flow
                }

                val userDetails = UserDetailsModel(
                    id = userId, name = name, email = email
                )

                firestore.collection("users").document(userId).set(userDetails).await()
                authResult?.user?.sendEmailVerification()?.await()
                emit(Resource.Success(userDetails))
            } catch (e: Exception) {
                logAuthIssueToCrashlytics(
                    e.message ?: "Unknown error from exception = ${e::class.java}",
                    AuthProvider.EMAIL.name
                )
                emit(Resource.Error(e))
            }
        }
    }

    override suspend fun registerWithGoogle(idToken: String): Flow<Resource<UserDetailsModel>> {
        return flow {
            try {
                emit(Resource.Loading())
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val authResult = auth.signInWithCredential(credential).await()
                val userId = authResult.user?.uid

                if (userId == null) {
                    val msg = "Sign up UserID not found"
                    logAuthIssueToCrashlytics(msg, AuthProvider.GOOGLE.name)
                    emit(Resource.Error(Exception(msg)))
                    return@flow
                }

                val userDetails = UserDetailsModel(
                    id = userId,
                    name = authResult.user?.displayName ?: "",
                    email = authResult.user?.email ?: "",
                )

                firestore.collection("users").document(userId).set(userDetails).await()
                emit(Resource.Success(userDetails))
            } catch (e: Exception) {
                logAuthIssueToCrashlytics(
                    e.message ?: "Unknown error from exception = ${e::class.java}",
                    AuthProvider.GOOGLE.name
                )
                emit(Resource.Error(e))
            }
        }
    }

    override suspend fun registerWithFacebook(token: String): Flow<Resource<UserDetailsModel>> {
        return flow {
            try {
                emit(Resource.Loading())
                val credential = FacebookAuthProvider.getCredential(token)
                val authResult = auth.signInWithCredential(credential).await()
                val userId = authResult.user?.uid

                if (userId == null) {
                    val msg = "Sign up UserID not found"
                    logAuthIssueToCrashlytics(msg, AuthProvider.FACEBOOK.name)
                    emit(Resource.Error(Exception(msg)))
                    return@flow
                }

                val userDetails = UserDetailsModel(
                    id = userId,
                    name = authResult.user?.displayName ?: "",
                    email = authResult.user?.email ?: "",
                )

                firestore.collection("users").document(userId).set(userDetails).await()
                emit(Resource.Success(userDetails))
            } catch (e: Exception) {
                logAuthIssueToCrashlytics(
                    e.message ?: "Unknown error from exception = ${e::class.java}",
                    AuthProvider.FACEBOOK.name
                )
                emit(Resource.Error(e))
            }
        }
    }

    override suspend fun sendUpdatePasswordEmail(email: String): Flow<Resource<String>> {
        return flow {
            try {
                emit(Resource.Loading())
                auth.sendPasswordResetEmail(email).await()
                emit(Resource.Success("Password reset email sent"))
            } catch (e: Exception) {
                emit(Resource.Error(e))
            }
        }
    }

    private fun logAuthIssueToCrashlytics(msg: String, provider: String) {
        CrashlyticsUtils.sendCustomLogToCrashlytics<LoginException>(
            msg,
            CrashlyticsUtils.LOGIN_KEY to msg,
            CrashlyticsUtils.LOGIN_PROVIDER to provider,
        )
    }

    override fun logout() {
        auth.signOut()
    }
}