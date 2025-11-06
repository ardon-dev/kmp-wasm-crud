package com.ardondev.contactsapp.feature.new_contact

import ContentCard
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ardondev.contactsapp.core.components.Avatar
import com.ardondev.contactsapp.core.components.CustomButton
import com.ardondev.contactsapp.core.components.CustomTextField
import com.ardondev.contactsapp.core.openFileExplorer
import contactsapp.composeapp.generated.resources.Res
import contactsapp.composeapp.generated.resources.blur
import io.ktor.util.collections.getValue
import org.jetbrains.compose.resources.painterResource
import org.w3c.files.FileReader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewContactDialog(
    viewModel: NewContactViewModel = viewModel { NewContactViewModel() },
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.contactAdded) {
        if (uiState.contactAdded) {
            onDismissRequest()
        }
    }

    Dialog(
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
        onDismissRequest = onDismissRequest
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {

            Box(Modifier) {
                Image(
                    painter = painterResource(Res.drawable.blur),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize().blur(10.dp)
                )
            }

            ContentCard(
                title = "Nuevo contacto",
                onDismissRequest = onDismissRequest,
                footer = {
                    Row {
                        Spacer(Modifier.weight(1f))
                        CustomButton(
                            outline = true,
                            text = "Cancel",
                            onClick = onDismissRequest
                        )
                        Spacer(Modifier.width(16.dp))
                        if (uiState.loading) {
                            CircularProgressIndicator(Modifier.size(24.dp))
                        } else {
                            CustomButton(
                                text = "Save",
                                onClick = viewModel::addContact
                            )
                        }
                    }
                },
                content = {

                    ProfilePhotoSection(
                        avatar = uiState.avatar,
                        onUploadClick = {
                            handleSelectedFile(
                                onSuccess = viewModel::updateAvatar,
                                onError = { e ->

                                }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )

                    Spacer(Modifier.height(16.dp))

                    CustomTextField(
                        label = "Name",
                        value = uiState.name,
                        onValueChange = viewModel::updateName,
                        leadingIcon = Icons.Outlined.Person,
                        modifier = Modifier.width(400.dp)
                    )

                    Spacer(Modifier.height(16.dp))

                    CustomTextField(
                        label = "Email",
                        value = uiState.email,
                        onValueChange = viewModel::updateEmail,
                        leadingIcon = Icons.Outlined.Email,
                        modifier = Modifier.width(400.dp)
                    )

                    Spacer(Modifier.height(16.dp))

                    CustomTextField(
                        label = "Phone number",
                        value = uiState.phone,
                        onValueChange = viewModel::updatePhone,
                        leadingIcon = Icons.Outlined.Phone,
                        modifier = Modifier.width(400.dp)
                    )
                }
            )

        }
    }

}

@Composable
private fun ProfilePhotoSection(
    avatar: String?,
    onUploadClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
        ) {

            Text(
                text = "Profile photo",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(16.dp))

            Avatar(avatar)

            Spacer(Modifier.height(16.dp))

            CustomButton(
                text = "Upload",
                onClick = onUploadClick,
                outline = true,
                leadingIcon = Icons.Outlined.FileUpload
            )

        }

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