package com.ardondev.contactsapp.feature.new_contact

data class NewContactUiState(
    val name: String = "",
    val nameError: String? = null,
    val phone: String = "",
    val phoneError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val validForm: Boolean = false,
    val avatar: String? = null,
    val avatarError: String? = null,
    val showCancelDialog: Boolean = false
)