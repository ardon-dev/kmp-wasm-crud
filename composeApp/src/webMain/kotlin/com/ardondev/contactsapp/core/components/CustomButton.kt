package com.ardondev.contactsapp.core.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import org.jetbrains.skia.FontWeight

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.defaultMinSize(
        minWidth = 120.dp,
        minHeight = 48.dp
    ),
    outline: Boolean = false,
    enabled: Boolean = true,
    loading: Boolean = false,
    leadingIcon: ImageVector? = null,
    leadingIconRotation: Float = 0f,
    trailingIcon: ImageVector? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    if (outline) {

        OutlinedButton(
            onClick = onClick,
            enabled = enabled and !loading,
            border = _root_ide_package_.androidx.compose.foundation.BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                contentColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                disabledContentColor = MaterialTheme.colorScheme.primary
            ),
            modifier = modifier
                .hoverable(
                    enabled = enabled and !loading,
                    interactionSource = interactionSource
                )
                .pointerHoverIcon(
                    icon = if (enabled and !loading) PointerIcon.Hand else PointerIcon.Default,
                    overrideDescendants = true
                )
        ) {

            if (loading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
            } else {
                CustomButtonContent(text, leadingIcon, leadingIconRotation, trailingIcon)
            }

        }

    } else {

        Button(
            onClick = onClick,
            modifier = modifier
                .hoverable(
                    enabled = enabled and !loading,
                    interactionSource = interactionSource
                )
                .pointerHoverIcon(
                    icon = if (enabled and !loading) PointerIcon.Hand else PointerIcon.Default,
                    overrideDescendants = true
                ),
            enabled = enabled and !loading,
            colors = ButtonDefaults.buttonColors(
                disabledContainerColor = ButtonDefaults.buttonColors().containerColor,
                disabledContentColor = ButtonDefaults.buttonColors().contentColor
            ),
        ) {

            if (loading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(18.dp)
                )
            } else {
                CustomButtonContent(text, leadingIcon, leadingIconRotation, trailingIcon)
            }

        }

    }

}

@Composable
private fun CustomButtonContent(
    text: String,
    leadingIcon: ImageVector? = null,
    leadingIconRotation: Float = 0f,
    trailingIcon: ImageVector? = null
) {
    leadingIcon?.let {
        Icon(
            imageVector = leadingIcon,
            contentDescription = null,
            modifier = Modifier
                .padding(end = 12.dp)
                .size(18.dp)
                .rotate(leadingIconRotation)
        )
    }

    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall.copy(
            fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
        )
    )

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