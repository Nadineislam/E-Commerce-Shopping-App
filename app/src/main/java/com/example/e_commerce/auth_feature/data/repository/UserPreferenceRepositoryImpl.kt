package com.example.e_commerce.auth_feature.data.repository

import com.example.e_commerce.auth_feature.data.datastore.UserPreferencesDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserPreferenceRepositoryImpl(
    private val userPreferencesDataSource: UserPreferencesDataSource
) : UserPreferenceRepository {
    override suspend fun saveLoginState(isLoggedIn: Boolean) {
        userPreferencesDataSource.saveLoginState(isLoggedIn)
    }

    override suspend fun saveUserID(userId: String) {
        userPreferencesDataSource.saveUserID(userId)
    }

    override suspend fun isUserLoggedIn(): Flow<Boolean> = userPreferencesDataSource.isUserLoggedIn

    override fun getUserID(): Flow<String?> = userPreferencesDataSource.userID

}