package com.ardondev.contactsapp.core.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

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
        label = if (label != null) {
            { Text(label) }
        } else null,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = if (selected) selectedIcon else defaultIcon,
                contentDescription = null
            )
        },
        colors = NavigationRailItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
            unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
            selectedTextColor = MaterialTheme.colorScheme.onPrimary,
            unselectedTextColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier
    )

}