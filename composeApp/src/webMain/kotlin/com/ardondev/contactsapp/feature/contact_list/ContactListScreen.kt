package com.ardondev.contactsapp.feature.contact_list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ardondev.contactsapp.core.components.ColumnDef
import com.ardondev.contactsapp.core.components.DynamicDataTable
import kotlinx.serialization.Serializable

@Serializable
object ContactListScreenRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListScreen(
    viewModel: ContactListViewModel = viewModel { ContactListViewModel() },
    onUnauthorized: () -> Unit,
    onAddClick: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    DynamicDataTable(
        isLoading = uiState.loading,
        error = uiState.error,
        data = uiState.contacts ?: emptyList(),
        columnDefs = listOf(
            ColumnDef<Contact>(
                header = "ID",
                accessor = { it.id.toString() },
                widthWeight = 0.2f
            ),
            ColumnDef<Contact>(
                header = "Name",
                accessor = { it.name.orEmpty() }
            ),
            ColumnDef<Contact>(
                header = "Phone",
                accessor = { it.phone.orEmpty() }
            ),
            ColumnDef<Contact>(
                header = "Email",
                accessor = { it.email.orEmpty() }
            )
        ),
        onRefreshClick = viewModel::getContacts,
        onAddClick = onAddClick,
        modifier = Modifier
            .fillMaxSize()
    )

}