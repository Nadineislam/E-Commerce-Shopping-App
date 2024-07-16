package com.example.e_commerce.auth_feature.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.auth_feature.data.models.UserDetailsModel
import com.example.e_commerce.auth_feature.domain.repository.AppDataStoreRepository
import com.example.e_commerce.auth_feature.domain.repository.UserPreferencesRepository
import com.example.e_commerce.auth_feature.domain.use_case.LoginUseCase
import com.example.e_commerce.auth_feature.domain.use_case.LoginWithFacebookUseCase
import com.example.e_commerce.auth_feature.domain.use_case.LoginWithGoogleUseCase
import com.example.e_commerce.auth_feature.domain.use_case.toUserDetailsPreferences
import com.example.e_commerce.core.extensions.isValidEmail
import com.example.e_commerce.core.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    private val loginWithFacebookUseCase: LoginWithFacebookUseCase,
    private val appDataStoreRepository: AppDataStoreRepository,
    private val userPreferenceRepository: UserPreferencesRepository,
) : ViewModel() {
    private val _loginState = MutableSharedFlow<Resource<UserDetailsModel>>()
    val loginState = _loginState.asSharedFlow()

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    private val isLoginIsValid: Flow<Boolean> = combine(email, password) { email, password ->
        email.isValidEmail() && password.length >= 6
    }

    fun loginWithEmailAndPassword() = viewModelScope.launch {
        val email = email.value
        val password = password.value
        if (isLoginIsValid.first()) {
            handleLoginFlow { loginUseCase(email, password) }
        } else {
            _loginState.emit(Resource.Error(Exception("Invalid email or password")))
        }
    }

    fun loginWithGoogle(idToken: String){
        handleLoginFlow { loginWithGoogleUseCase(idToken) }
    }
    fun loginWithFacebook(token:String){
        handleLoginFlow { loginWithFacebookUseCase(token) }

    }
    private fun handleLoginFlow(loginFlow: suspend () -> Flow<Resource<UserDetailsModel>>) =
        viewModelScope.launch(IO) {
            loginFlow().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        savePreferenceData(resource.data!!)
                        _loginState.emit(Resource.Success(resource.data))
                    }

                    else -> _loginState.emit(resource)
                }
            }
        }

    private suspend fun savePreferenceData(userDetailsModel: UserDetailsModel) {
        appDataStoreRepository.saveLoginState(true)
        val country = userPreferenceRepository.getUserCountry().first()
        userPreferenceRepository.updateUserDetails(userDetailsModel.toUserDetailsPreferences(country))
    }
}
