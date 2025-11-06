package com.ardondev.contactsapp.feature.contact_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ardondev.contactsapp.core.KtorClient
import com.ardondev.contactsapp.feature.login.LoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContactListViewModel: ViewModel() {

    private val _loginRepository = LoginRepository()
    private val _contactListRepository = ContactListRepository()

    private val _uiState = MutableStateFlow(ContactListUiState())
    val uiState: StateFlow<ContactListUiState> = _uiState.asStateFlow()

    fun updateQuery(value: String) {
        _uiState.value = uiState.value.copy(query = value)
    }

    fun showAddDialog() {
        _uiState.value = uiState.value.copy(showAddDialog = true)
    }

    fun hideAddDialog() {
        _uiState.value = uiState.value.copy(showAddDialog = false)
    }

    fun getContacts() {
        if (uiState.value.loading) return

        viewModelScope.launch {
            _uiState.value = uiState.value.copy(
                loading = true,
                error = null
            )

            _loginRepository.refreshToken().fold(
                onSuccess = {
                    _contactListRepository.getContacts().fold(
                        onSuccess = { data ->
                            _uiState.value = uiState.value.copy(
                                loading = false,
                                error = null,
                                contacts = data
                            )
                        },
                        onFailure = { error ->
                            _uiState.value = uiState.value.copy(
                                loading = false,
                                error = error.message,
                                contacts = null,
                                unauthorized = error is KtorClient.UnauthorizedException
                            )
                        }
                    )
                },
                onFailure = { error ->
                    _uiState.value = uiState.value.copy(
                        loading = false,
                        error = error.message,
                        contacts = null
                    )
                }
            )
        }
    }

    init {
        getContacts()
    }

}