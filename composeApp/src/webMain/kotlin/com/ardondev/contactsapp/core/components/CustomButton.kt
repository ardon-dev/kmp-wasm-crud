package com.ardondev.contactsapp.core.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.defaultMinSize(
        minWidth = 120.dp
    ),
    outline: Boolean = false,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
) {
    if (outline) {

        OutlinedButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            shape = MaterialTheme.shapes.medium
        ) {

            CustomButtonContent(text, leadingIcon, trailingIcon)

        }

    } else {

        Button(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            shape = MaterialTheme.shapes.medium
        ) {

            CustomButtonContent(text, leadingIcon, trailingIcon)

        }

    }

}

@Composable
private fun CustomButtonContent(
    text: String,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null
) {
    leadingIcon?.let {
        Icon(
            imageVector = leadingIcon,
            contentDescription = null,
            modifier = Modifier
                .padding(end = 12.dp)
                .size(18.dp)
        )
    }

    Text(text)

    trailingIcon?.let {
        Icon(
            imageVector = trailingIcon,
            contentDescription = null,
            modifier = Modifier
                .padding(end = 12.dp)
                .padding(start = 12.dp)
        )
    }
}