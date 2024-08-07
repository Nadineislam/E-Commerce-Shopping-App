package com.example.e_commerce.auth_feature.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.e_commerce.auth_feature.data.datastore.DataStoreKeys.IS_USER_LOGGED_IN
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppPreferencesDataStore @Inject constructor(private val context: Context) {
    suspend fun saveLoginState(isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_USER_LOGGED_IN] = isLoggedIn
        }
    }

    val isUserLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_USER_LOGGED_IN] ?: false
        }
}