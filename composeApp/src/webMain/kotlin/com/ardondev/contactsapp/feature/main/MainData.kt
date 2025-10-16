package com.ardondev.contactsapp.feature.main

import com.ardondev.contactsapp.feature.config.ConfigScreenRoute
import com.ardondev.contactsapp.feature.contacts.ContactListScreenRoute

// UI

enum class RailItem(val label: String, val route: Any) {
    Contacts("Contacts", ContactListScreenRoute),
    Config("Settings", ConfigScreenRoute)
}

data class MainUiState(
    val railItem: RailItem = RailItem.Contacts
)