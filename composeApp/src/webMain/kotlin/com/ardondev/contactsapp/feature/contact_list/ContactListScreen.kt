package com.ardondev.contactsapp.feature.contact_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ardondev.contactsapp.core.components.ColumnDef
import com.ardondev.contactsapp.core.components.DynamicDataTable
import kotlinx.serialization.Serializable

@Serializable
object ContactListScreenRoute

@Composable
fun ContactListScreen(
    viewModel: ContactListViewModel = viewModel { ContactListViewModel() },
    onUnauthorized: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.loading -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(Modifier.size(32.dp))
            }
        }

        uiState.error != null -> {

        }

        uiState.contacts != null -> {
            DynamicDataTable(
                data = uiState.contacts!!,
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
                onAddClick = {}
            )
        }

    }

}