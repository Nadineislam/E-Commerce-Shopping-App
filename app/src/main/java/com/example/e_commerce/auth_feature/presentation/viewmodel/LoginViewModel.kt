package com.example.e_commerce.auth_feature.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.auth_feature.domain.use_case.LoginUseCase
import com.example.e_commerce.core.extensions.isValidEmail
import com.example.e_commerce.core.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    private val _loginState = MutableSharedFlow<Resource<String>>()
    val loginState = _loginState.asSharedFlow()

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    private val isLoginIsValid: Flow<Boolean> = combine(email, password) { email, password ->
        email.isValidEmail() && password.length >= 6
    }

    fun login() = viewModelScope.launch {
        val email = email.value
        val password = password.value
        if (isLoginIsValid.first()) {
            _loginState.emit(Resource.Loading())
            loginUseCase(email, password).onEach { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _loginState.emit(Resource.Loading())
                    }

                    is Resource.Success -> {
                        //       savePreferenceData(resource.data!!)
                        _loginState.emit(Resource.Success(resource.data ?: "Empty User Id"))
                    }

                    is Resource.Error -> {
                        _loginState.emit(
                            Resource.Error(
                                resource.exception ?: Exception("Unknown Error")
                            )
                        )
                    }
                }
            }.launchIn(viewModelScope)
        } else {
            _loginState.emit(Resource.Error(Exception("Invalid email or password")))
        }
    }

}