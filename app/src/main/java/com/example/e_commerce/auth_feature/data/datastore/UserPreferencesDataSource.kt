package com.example.e_commerce.auth_feature.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.e_commerce.auth_feature.data.datastore.DataStoreKeys.IS_USER_LOGGED_IN
import com.example.e_commerce.auth_feature.data.datastore.DataStoreKeys.USER_ID
import com.example.e_commerce.auth_feature.data.datastore.DataStoreKeys.USER_PREFERENCES
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesDataSource @Inject constructor(private val context:Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_PREFERENCES)

    suspend fun saveLoginState(isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_USER_LOGGED_IN] = isLoggedIn
        }
    }

    suspend fun saveUserID(userId: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = userId
        }
    }

    val isUserLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_USER_LOGGED_IN] ?: false
        }

    val userID: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_ID]

        }
}