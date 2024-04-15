package com.example.e_commerce.auth_feature.data.repository

import com.example.e_commerce.auth_feature.data.datastore.AppPreferencesDataStore
import com.example.e_commerce.auth_feature.domain.repository.AppDataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppDataStoreRepositoryImpl @Inject constructor(private val appPreferencesDataStore: AppPreferencesDataStore) :
    AppDataStoreRepository {
    override suspend fun saveLoginState(isLoggedIn: Boolean) {
        appPreferencesDataStore.saveLoginState(isLoggedIn)
    }

    override suspend fun isLoggedIn(): Flow<Boolean> {
        return appPreferencesDataStore.isUserLoggedIn
    }
}