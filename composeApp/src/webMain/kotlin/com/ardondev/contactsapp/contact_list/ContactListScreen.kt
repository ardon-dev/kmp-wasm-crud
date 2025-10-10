package com.ardondev.contactsapp.contact_list

import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

@Serializable
object ContactsRoute

data class ContactListUiState(
    val contacts: List<Any> = emptyList(),
)

@Composable
fun ContactListScreen() {

}