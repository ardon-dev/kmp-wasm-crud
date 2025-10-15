package com.ardondev.contactsapp.feature.contacts

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