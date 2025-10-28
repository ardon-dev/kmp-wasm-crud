package com.ardondev.contactsapp.feature.contact_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ardondev.contactsapp.core.components.ColumnDef
import com.ardondev.contactsapp.core.components.CustomButton
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

    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)) {

        Row(Modifier.padding(24.dp)) {

            Column {
                Text(
                    text = "Contacts",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Black
                    )
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Manage your contacts",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(Modifier.weight(1f))

            CustomButton(
                outline = true,
                text = "Refresh",
                leadingIcon = Icons.Default.Sync,
                onClick = {},
                modifier = Modifier
                    .height(56.dp)
            )

            Spacer(Modifier.width(12.dp))

            CustomButton(
                text = "Add contact",
                leadingIcon = Icons.Default.Add,
                onClick = {},
                modifier = Modifier
                    .height(56.dp)
            )

        }

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
                .padding(24.dp)
        )

    }

}