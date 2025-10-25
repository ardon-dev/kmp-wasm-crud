package com.ardondev.contactsapp.feature.new_contact

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ardondev.contactsapp.core.components.*
import com.ardondev.contactsapp.core.openFileExplorer
import kotlinx.serialization.Serializable
import org.w3c.files.FileReader

@Serializable
object NewContactScreenRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewContactScreen(
    viewModel: NewContactViewModel = viewModel { NewContactViewModel() },
    onNavigateBack: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                title = {
                    Text("New contact", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black))
                },
                actions = {

                    CustomButton(
                        text = "Cancel",
                        outline = true,
                        leadingIcon = Icons.Outlined.Close,
                        onClick = viewModel::showCancelDialog
                    )

                    Spacer(Modifier.width(12.dp))

                    CustomButton(
                        text = "Save",
                        leadingIcon = Icons.Outlined.Done,
                        onClick = viewModel::validate
                    )

                    Spacer(Modifier.width(12.dp))

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

                    CustomTextField(
                        label = "Name",
                        value = uiState.name,
                        singleLine = true,
                        isError = uiState.nameError != null,
                        supportingText = uiState.nameError,
                        onValueChange = viewModel::updateName,
                        leadingIcon = Icons.Outlined.Person,
                        modifier = Modifier.width(400.dp)
                    )

                    Spacer(Modifier.height(16.dp))

                    CustomTextField(
                        label = "Email",
                        value = uiState.email,
                        singleLine = true,
                        isError = uiState.emailError != null,
                        supportingText = uiState.emailError,
                        leadingIcon = Icons.Outlined.Email,
                        onValueChange = viewModel::updateEmail,
                        modifier = Modifier.width(400.dp)
                    )

                    Spacer(Modifier.height(16.dp))

                    CustomTextField(
                        label = "Phone number",
                        value = uiState.phone,
                        singleLine = true,
                        isError = uiState.phoneError != null,
                        supportingText = uiState.phoneError,
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

                    Avatar(uiState.avatar)

                    Spacer(Modifier.height(24.dp))

                    // Subir archivo
                    OutlinedButton(
                        onClick = {
                            handleSelectedFile(
                                onSuccess = viewModel::updateAvatar,
                                onError = viewModel::updateAvatarError
                            )
                        }
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {

                            Icon(
                                imageVector = Icons.Default.UploadFile,
                                contentDescription = null
                            )

                            Spacer(Modifier.width(16.dp))

                            Text("Subir archivo")
                        }

                    }

                }
            }

        }
    }

    if (uiState.showCancelDialog) {
        CustomAlertDialog(
            title = "Cancelar",
            text = "Se perderá la información ingresada. ¿Deseas cancelar?",
            onDismissRequest = {
                viewModel.hideCancelDialog()
            },
            onPositiveButtonClick = {
                viewModel.hideCancelDialog()
                onNavigateBack()
            },
            onNegativeButtonClick = {
                viewModel.hideCancelDialog()
            }
        )
    }
}

@OptIn(ExperimentalWasmJsInterop::class)
private fun handleSelectedFile(
    onSuccess: (base64String: String) -> Unit,
    onError: (message: String) -> Unit
) {
    openFileExplorer(
        accept = "image/*",
        onFileSelected = { file ->
            if (file != null) {
                if (file.size.toDouble() == 0.0) {
                    onError("The file is empty.")
                    return@openFileExplorer
                }

                val reader = FileReader()
                reader.onload = {
                    val result = reader.result
                    if (result != null) {
                        onSuccess(result.toString())
                    } else {
                        onError("Error to convert file.")
                    }
                }

                reader.readAsDataURL(file)
            } else {
                print("No file selected.")
            }
        }
    )
}