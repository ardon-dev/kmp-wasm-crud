package com.ardondev.contactsapp.feature.new_contact

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ardondev.contactsapp.core.components.CustomCard
import com.ardondev.contactsapp.core.components.CustomTextField
import com.ardondev.contactsapp.core.openFileExplorer
import kotlinx.serialization.Serializable

@Serializable
object NewContactScreenRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewContactScreen(
    viewModel: NewContactViewModel = viewModel { NewContactViewModel() },
    onNavigateBack: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Text("New contact")
                },
                actions = {
                    SmallFloatingActionButton({}) {
                        Row {
                            Icon(Icons.Default.Done, null)
                        }
                    }
                }
            )
        }
    ) { paddingValues ->

        Row(
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding() + 24.dp,
                bottom = paddingValues.calculateTopPadding() + 24.dp,
                start = 24.dp,
                end = 24.dp
            )
        ) {

            CustomCard(
                headerText = "Contact info",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .padding(24.dp)
                ) {

                    Text(
                        text = "Fill the contact information below:",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(Modifier.height(24.dp))

                    CustomTextField(
                        label = "Name",
                        value = uiState.name,
                        singleLine = true,
                        onValueChange = viewModel::updateName,
                        leadingIcon = Icons.Outlined.Person,
                        modifier = Modifier.width(400.dp)
                    )

                    Spacer(Modifier.height(16.dp))

                    CustomTextField(
                        label = "Email",
                        value = uiState.email,
                        singleLine = true,
                        leadingIcon = Icons.Outlined.Email,
                        onValueChange = viewModel::updateEmail,
                        modifier = Modifier.width(400.dp)
                    )

                    Spacer(Modifier.height(16.dp))

                    CustomTextField(
                        label = "Phone number",
                        value = uiState.phone,
                        singleLine = true,
                        leadingIcon = Icons.Outlined.Phone,
                        onValueChange = viewModel::updatePhone,
                        modifier = Modifier.width(250.dp)
                    )

                }
            }

            Spacer(Modifier.width(24.dp))

            CustomCard(
                headerText = "Profile photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {

                    Image(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(100.dp)
                    )

                    Spacer(Modifier.height(24.dp))

                    OutlinedButton(
                        onClick = {
                            openFileExplorer(
                                accept = "image/*",
                                onFileSelected = { file ->
                                    println("File: ${file?.name}")
                                }
                            )
                        }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.UploadFile, null)
                            Spacer(Modifier.width(16.dp))
                            Text("Subir archivo")
                        }
                    }

                }
            }

        }
    }
}