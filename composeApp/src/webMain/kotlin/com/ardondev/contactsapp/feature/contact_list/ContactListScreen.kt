package com.ardondev.contactsapp.feature.contact_list

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Sync
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ardondev.contactsapp.core.components.ColumnDef
import com.ardondev.contactsapp.core.components.CustomButton
import com.ardondev.contactsapp.core.components.DynamicDataTable
import com.ardondev.contactsapp.feature.new_contact.NewContactDialog
import kotlinx.serialization.Serializable

@Serializable
object ContactListScreenRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListScreen(
    viewModel: ContactListViewModel = viewModel { ContactListViewModel() },
    onShowModal: (Boolean) -> Unit,
    onUnauthorized: () -> Unit,
    onAddClick: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)) {

        DynamicDataTable(
            title = "Contacts",
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
            actions = {

                val rotation = rememberInfiniteTransition().animateFloat(
                    initialValue = 360f,
                    targetValue = 0f,
                    animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                        animation = androidx.compose.animation.core.tween(
                            durationMillis = 1000,
                            easing = androidx.compose.animation.core.LinearEasing
                        ),
                        repeatMode = androidx.compose.animation.core.RepeatMode.Restart
                    )
                )

                CustomButton(
                    outline = true,
                    enabled = !uiState.loading,
                    text = "Refresh",
                    leadingIcon = Icons.Rounded.Sync,
                    leadingIconRotation = if (uiState.loading) rotation.value else 0f,
                    onClick = viewModel::getContacts
                )

                CustomButton(
                    text = "Add",
                    enabled = !uiState.loading,
                    leadingIcon = Icons.Rounded.Add,
                    onClick = viewModel::showAddDialog
                )

            },
            onRowClick = { data ->
                println(data.name)
            },
            modifier = Modifier
                .fillMaxSize()
        )

    }

    if (uiState.showAddDialog) {
        onShowModal(true)
        NewContactDialog(
            onDismissRequest = {
                viewModel.hideAddDialog()
                onShowModal(false)
            }
        )
    }

}