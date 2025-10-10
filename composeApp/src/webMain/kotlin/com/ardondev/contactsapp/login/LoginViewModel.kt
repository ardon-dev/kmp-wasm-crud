package com.ardondev.contactsapp.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun validate() {
        var emailError: String? = null
        var passwordError: String? = null

        if (_uiState.value.email.isBlank()) {
            emailError = "El email no puede estar vacío"
        }

        if (_uiState.value.password.isBlank()) {
            passwordError = "La contraseña no puede estar vacía"
        }

        _uiState.value = _uiState.value.copy(
            emailError = emailError,
            passwordError = passwordError
        )

        if (emailError == null && passwordError == null) {
            login()
        }
    }

    fun login() {
        _uiState.value = _uiState.value.copy(loading = true)
    }

}