package com.ardondev.contactsapp.core.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun CustomNavigationRailItem(
    selected: Boolean = false,
    defaultIcon: ImageVector,
    selectedIcon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null
) {

    NavigationRailItem(
        selected = selected,
        alwaysShowLabel = false,
        onClick = onClick,
        icon = {
            Image(
                imageVector = if (selected) selectedIcon else defaultIcon,
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .background(
                        color = if (selected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                        shape = MaterialTheme.shapes.medium
                    )
            )
        },
        colors = NavigationRailItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
            unselectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
            selectedTextColor = MaterialTheme.colorScheme.onPrimary,
            unselectedTextColor = MaterialTheme.colorScheme.onSurface,
            indicatorColor = Color.Transparent
        ),
        modifier = modifier
    )

}