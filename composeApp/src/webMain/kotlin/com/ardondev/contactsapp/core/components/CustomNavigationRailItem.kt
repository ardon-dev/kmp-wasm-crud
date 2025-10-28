package com.ardondev.contactsapp.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.stylusHoverIcon
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import androidx.compose.runtime.getValue

@Composable
fun CustomNavigationRailItem(
    selected: Boolean = false,
    defaultIcon: ImageVector,
    selectedIcon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    NavigationRailItem(
        selected = selected,
        alwaysShowLabel = false,
        interactionSource = NoInteractionSource(),
        onClick = onClick,
        icon = {
            Icon(
                imageVector = if (selected) selectedIcon else defaultIcon,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .stylusHoverIcon(
                        icon = PointerIcon.Hand,
                        overrideDescendants = true
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
            .size(56.dp)
            .background(
                color = if (selected) {
                    MaterialTheme.colorScheme.primaryContainer
                } else if (isHovered) {
                    MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
                } else {
                    Color.Transparent
                },
                shape = MaterialTheme.shapes.large
            )
            .padding(8.dp)
            .hoverable(interactionSource)
    )

}

class NoInteractionSource : MutableInteractionSource {
    override val interactions: Flow<Interaction> = emptyFlow()
    override suspend fun emit(interaction: Interaction) {}
    override fun tryEmit(interaction: Interaction) = true
}