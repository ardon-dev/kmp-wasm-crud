package com.ardondev.contactsapp.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ardondev.contactsapp.core.Session
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _loginRepository = LoginRepository()

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun updateError(error: String?) {
        _uiState.value = _uiState.value.copy(error = error)
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
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                loading = true,
                error = null,
                loginResponse = null
            )

            _loginRepository.login(
                email = uiState.value.email.trim(),
                password = uiState.value.password.trim()
            ).fold(
                onSuccess = { response ->
                    saveSession(response)
                    _uiState.value = uiState.value.copy(
                        loading = false,
                        error = null,
                        loginResponse = response
                    )
                },
                onFailure = { error ->
                    _uiState.value = uiState.value.copy(
                        loading = false,
                        error = error.message,
                        loginResponse = null
                    )
                }
            )

        }
    }

    private fun saveSession(response: LoginResponse) {
        Session.saveValue(Session.KEY_TOKEN, response.accessToken.orEmpty())
        Session.saveValue(Session.KEY_EXPIRES_AT, response.expiresAt.toString())
    }

}