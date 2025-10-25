package com.ardondev.contactsapp.feature.new_contact

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NewContactViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(NewContactUiState())
    val uiState: StateFlow<NewContactUiState> = _uiState.asStateFlow()

    fun updateName(name: String) {
        _uiState.value = uiState.value.copy(name = name)
    }

    fun updatePhone(phone: String) {
        _uiState.value = uiState.value.copy(phone = phone)
    }

    fun updateEmail(email: String) {
        _uiState.value = uiState.value.copy(email = email)
    }

    fun updateAvatar(avatar: String?) {
        println(avatar)
        _uiState.value = uiState.value.copy(
            avatarError = null,
            avatar = avatar
        )
    }

    fun showCancelDialog() {
        uiState.value.let {
            if (it.name.isNotBlank() or it.phone.isNotBlank() or it.email.isNotBlank()) {
                _uiState.value = uiState.value.copy(showCancelDialog = true)
            }
        }
    }

    fun hideCancelDialog() {
        _uiState.value = uiState.value.copy(showCancelDialog = false)
    }

    fun updateAvatarError(avatarError: String?) {
        println(avatarError)
        _uiState.value = uiState.value.copy(
            avatar = null,
            avatarError = avatarError
        )
    }

    fun validate() {
        var nameError: String? = null
        var phoneError: String? = null
        var emailError: String? = null

        if (_uiState.value.name.isBlank()) {
            nameError = "El nombre no puede estar vacío"
        }

        if (_uiState.value.phone.isBlank()) {
            phoneError = "El número de teléfono no puede estar vacía"
        }

        if (_uiState.value.email.isBlank()) {
            emailError = "El correo electrónico no puede estar vacía"
        }

        _uiState.value = _uiState.value.copy(
            emailError = emailError,
            phoneError = phoneError,
            nameError = nameError
        )
    }

}