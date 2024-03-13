package com.example.e_commerce.auth_feature.representation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.e_commerce.auth_feature.data.repository.UserPreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userPreferencesRepository: UserPreferenceRepository) :
    ViewModel() {
    suspend fun isUserLoggedIn() = userPreferencesRepository.isUserLoggedIn()

    fun setIsLoggedIn(b: Boolean) {
        viewModelScope.launch(IO) {
            userPreferencesRepository.saveLoginState(b)
        }
    }
}
