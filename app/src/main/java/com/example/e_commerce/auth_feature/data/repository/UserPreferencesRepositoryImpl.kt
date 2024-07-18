package com.example.e_commerce.auth_feature.data.repository

import android.content.Context
import android.util.Log
import com.example.e_commerce.auth_feature.data.datastore.userDetailsDataStore
import com.example.e_commerce.auth_feature.data.user.CountryData
import com.example.e_commerce.auth_feature.data.user.UserDetailsPreferences
import com.example.e_commerce.auth_feature.domain.repository.UserPreferencesRepository
import com.example.e_commerce.auth_feature.presentation.model.CountryUIModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(private val context: Context) :
    UserPreferencesRepository {
    override fun getUserDetails(): Flow<UserDetailsPreferences> {
        return context.userDetailsDataStore.data
    }

    override suspend fun updateUserId(userId: String) {
        context.userDetailsDataStore.updateData { preferences ->
            preferences.toBuilder().setId(userId).build()
        }
    }

    override suspend fun getUserId(): Flow<String> {
        return context.userDetailsDataStore.data.map { it.id }
    }

    override suspend fun clearUserPreferences() {
        context.userDetailsDataStore.updateData { preferences ->
            preferences.toBuilder().clear().build()
        }
    }

    override suspend fun updateUserDetails(userDetailsPreferences: UserDetailsPreferences) {
        context.userDetailsDataStore.updateData { userDetailsPreferences }
    }

    override suspend fun saveUserCountry(countryId: CountryUIModel) {
        val countryData = CountryData.newBuilder().setId(countryId.id).setCode(countryId.code)
            .setName(countryId.name).setCurrency(countryId.currency)
            .setCurrencySymbol(countryId.currencySymbol).build()
        context.userDetailsDataStore.updateData { preferences ->
            preferences.toBuilder().setCountry(countryData).build()
        }
    }

    override fun getUserCountry(): Flow<CountryData> {
        return context.userDetailsDataStore.data.map { it.country }
    }
}