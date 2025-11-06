package com.ardondev.contactsapp.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ardondev.contactsapp.feature.login.LoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val _loginRepository = LoginRepository()

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun updateRailItem(railItem: RailItem) {
        _uiState.value = uiState.value.copy(railItem = railItem)
    }

    fun showLogoutDialog() {
        _uiState.value = uiState.value.copy(showLogoutDialog = true)
    }

    fun hideLogoutDialog() {
        _uiState.value = uiState.value.copy(showLogoutDialog = false)
    }

    fun blur(blur: Boolean) {
        _uiState.value = uiState.value.copy(blur = blur)
    }

    fun logout() {
        viewModelScope.launch {
            _loginRepository.logout().fold(
                onSuccess = {
                    _uiState.value = uiState.value.copy(logout = true)
                },
                onFailure = {
                    _uiState.value = uiState.value.copy(logout = true)
                }
            )
        }
    }

}