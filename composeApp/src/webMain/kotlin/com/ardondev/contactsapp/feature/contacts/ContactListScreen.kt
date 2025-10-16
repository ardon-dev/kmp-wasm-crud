package com.ardondev.contactsapp.feature.contacts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.serialization.Serializable

@Serializable
object ContactListScreenRoute

@Composable
fun ContactListScreen(
    viewModel: ContactListViewModel = viewModel { ContactListViewModel() }
) {

    val uiState by viewModel.uiState.collectAsState()

}