package com.ardondev.contactsapp.feature.new_contact

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


// UI

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
    val showCancelDialog: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null,
    val contactAdded: Boolean = false
)

// API

@Serializable
data class NewContactRequest(
    @SerialName("name"     ) var name   : String? = null,
    @SerialName("phone"    ) var phone  : String? = null,
    @SerialName("email"    ) var email  : String? = null,
    @SerialName("photo_url") var avatar : String? = null,
)
