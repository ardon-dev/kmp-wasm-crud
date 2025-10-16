package com.ardondev.contactsapp.feature.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ardondev.contactsapp.core.components.CustomTextField
import kotlinx.serialization.Serializable

@Serializable
object LoginRoute

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel { LoginViewModel() },
    onLoginSuccess: () -> Unit = {}
) {

    val uiState by viewModel.uiState.collectAsState()
    var showPassword by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {

        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(
                        vertical = 24.dp,
                        horizontal = 48.dp
                    )
            ) {

                Image(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(100.dp)
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Inicio de sesión",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(Modifier.height(24.dp))

                CustomTextField(
                    value = uiState.email,
                    onValueChange = viewModel::updateEmail,
                    keyboardType = KeyboardType.Email,
                    singleLine = true,
                    label = "Email",
                    leadingIcon = Icons.Outlined.Email,
                    isError = uiState.emailError != null,
                    supportingText = uiState.emailError
                )

                Spacer(Modifier.height(16.dp))

                CustomTextField(
                    value = uiState.password,
                    onValueChange = viewModel::updatePassword,
                    keyboardType = KeyboardType.Password,
                    singleLine = true,
                    label = "Password",
                    visualTransformation = if (showPassword) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    leadingIcon = Icons.Outlined.Lock,
                    isError = uiState.passwordError != null,
                    supportingText = uiState.passwordError
                )

                Spacer(Modifier.height(24.dp))

                if (uiState.loading) {
                    CircularProgressIndicator(Modifier.size(24.dp))
                } else {
                    Button(
                        onClick = viewModel::validate,
                        enabled = !uiState.loading
                    ) {
                        Text("Ingresar")
                    }
                }

            }

        }

    }

    uiState.error?.let { error ->
        AlertDialog(
            title = {
                Text("Error")
            },
            text = {
                Text(error)
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateError(null)
                    }
                ) {
                    Text("Aceptar")
                }
            },
            onDismissRequest = {
                viewModel.updateError(null)
            }
        )
    }

    LaunchedEffect(uiState.loginResponse) {
        if (uiState.loginResponse != null) {
            onLoginSuccess()
        }
    }

}