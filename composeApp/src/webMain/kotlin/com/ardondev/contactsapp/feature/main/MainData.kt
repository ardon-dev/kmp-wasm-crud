package com.ardondev.contactsapp.feature.main

import com.ardondev.contactsapp.feature.config.ConfigScreenRoute
import com.ardondev.contactsapp.feature.contact_list.ContactListScreenRoute

// UI

enum class RailItem(val label: String, val route: Any) {
    Contacts("Contacts", ContactListScreenRoute),
    Config("Settings", ConfigScreenRoute)
}

data class MainUiState(
    val railItem: RailItem = RailItem.Contacts,
    val showLogoutDialog: Boolean = false,
    val logout: Boolean = false,
    val blur: Boolean = false
)