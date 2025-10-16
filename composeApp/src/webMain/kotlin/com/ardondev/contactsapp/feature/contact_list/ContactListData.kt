package com.ardondev.contactsapp.feature.contact_list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// UI

data class ContactListUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val contacts: List<Contact>? = null,
    val unauthorized: Boolean = false,
)

// API

@Serializable
data class Contact(
    @SerialName("id"         ) var id         : Int?     = null,
    @SerialName("created_at" ) var createdAt  : String?  = null,
    @SerialName("name"       ) var name       : String?  = null,
    @SerialName("phone"      ) var phone      : String?  = null,
    @SerialName("email"      ) var email      : String?  = null,
    @SerialName("photo_url"  ) var photoUrl   : String?  = null,
    @SerialName("is_favorite") var isFavorite : Boolean? = null,
    @SerialName("user_uuid"  ) var userUuid   : String?  = null
)