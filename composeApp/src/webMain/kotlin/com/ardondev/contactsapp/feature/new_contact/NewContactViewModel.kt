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

}