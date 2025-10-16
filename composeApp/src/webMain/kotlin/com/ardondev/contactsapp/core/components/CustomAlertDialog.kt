package com.ardondev.contactsapp.core.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CustomAlertDialog(
    title: String? = null,
    text: String? = null,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    positiveButtonText: String = "Ok",
    negativeButtonText: String = "Cancel",
    onPositiveButtonClick: (() -> Unit)? = null,
    onNegativeButtonClick: (() -> Unit)? = null
) {
    AlertDialog(
        title = if (title != null) {
            { Text(title) }
        } else null,
        text = if (text != null) {
            { Text(text) }
        } else null,
        onDismissRequest = onDismissRequest,
        confirmButton = {
            onPositiveButtonClick?.let {
                TextButton(onClick = it) {
                    Text(positiveButtonText)
                }
            }
        },
        dismissButton = {
            onNegativeButtonClick?.let {
                TextButton(onClick = it) {
                    Text(negativeButtonText)
                }
            }
        },
        modifier = modifier
    )
}