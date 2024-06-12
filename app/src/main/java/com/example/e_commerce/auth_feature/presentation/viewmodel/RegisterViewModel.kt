package com.example.e_commerce.auth_feature.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.auth_feature.data.models.UserDetailsModel
import com.example.e_commerce.auth_feature.domain.repository.FirebaseAuthRepository
import com.example.e_commerce.core.extensions.isValidEmail
import com.example.e_commerce.core.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
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
class RegisterViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository
) : ViewModel() {
    private val _registerState = MutableSharedFlow<Resource<UserDetailsModel>>()
    val registerState = _registerState.asSharedFlow()

    val name = MutableStateFlow("")
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val confirmPassword = MutableStateFlow("")

    private val isRegisterIsValid = combine(
        name, email, password, confirmPassword
    ) { name, email, password, confirmPassword ->
        email.isValidEmail() && password.length >= 6 && name.isNotEmpty() && confirmPassword.isNotEmpty() && password == confirmPassword
    }
    private val job: CoroutineScope = CoroutineScope(IO)
    fun registerWithEmailAndPassword() = viewModelScope.launch(IO) {
        val name = name.value
        val email = email.value
        val password = password.value
        if (isRegisterIsValid.first()) {
            authRepository.registerWithEmailAndPassword(name, email, password).onEach {
                _registerState.emit(it)
            }.launchIn(job)

        }
    }

    fun signUpWithGoogle(idToken: String) = viewModelScope.launch {
        authRepository.registerWithGoogle(idToken).collect {
            _registerState.emit(it)
        }
    }

    fun registerWithFacebook(token: String) = viewModelScope.launch {
        authRepository.registerWithFacebook(token).collect {
            _registerState.emit(it)
        }
    }
}