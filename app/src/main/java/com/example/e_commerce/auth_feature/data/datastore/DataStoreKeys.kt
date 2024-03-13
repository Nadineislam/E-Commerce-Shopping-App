package com.example.e_commerce.auth_feature.data.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object DataStoreKeys {
    const val USER_PREFERENCES = "user_preferences"
    val IS_USER_LOGGED_IN = booleanPreferencesKey("is_user_logged_in")
    val USER_ID = stringPreferencesKey("user_id")
}