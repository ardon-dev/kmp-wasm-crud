package com.ardondev.contactsapp.core.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.ardondev.contactsapp.core.decodeBase64ToByteArray
import com.ardondev.contactsapp.core.toImageBitmap

@Composable
fun Avatar(
    base64String: String? = null
) {
    if (base64String != null) {
        val byteArray = base64String.substringAfter("base64,").decodeBase64ToByteArray()
        val bitmap = byteArray.toImageBitmap()

        Image(
            bitmap = bitmap,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant
                    ),
                    shape = CircleShape
                )
        )
    } else {
        Image(
            imageVector = Icons.Filled.AccountCircle,
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
    }
}