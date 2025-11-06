package com.ardondev.contactsapp.feature.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.PowerSettingsNew
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ardondev.contactsapp.core.Session
import com.ardondev.contactsapp.core.components.CustomAlertDialog
import com.ardondev.contactsapp.core.components.CustomNavigationRailItem
import com.ardondev.contactsapp.feature.config.ConfigScreen
import com.ardondev.contactsapp.feature.config.ConfigScreenRoute
import com.ardondev.contactsapp.feature.contact_list.ContactListScreen
import com.ardondev.contactsapp.feature.contact_list.ContactListScreenRoute
import com.ardondev.contactsapp.feature.new_contact.NewContactScreen
import com.ardondev.contactsapp.feature.new_contact.NewContactScreenRoute
import kotlinx.browser.window

@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel { MainViewModel() }
) {

    val uiState by viewModel.uiState.collectAsState()
    val navController = rememberNavController()

    Row(
        modifier = Modifier
            .fillMaxSize()
    ) {

        NavigationRail(
            header = { Box {} }
        ) {

            CustomNavigationRailItem(
                selected = uiState.railItem == RailItem.Contacts,
                label = RailItem.Contacts.label,
                onClick = {
                    viewModel.updateRailItem(RailItem.Contacts)
                },
                defaultIcon = Icons.Outlined.AccountBox,
                selectedIcon = Icons.Rounded.AccountBox
            )

            Spacer(Modifier.weight(1f))

            OutlinedIconButton(
                onClick = viewModel::showLogoutDialog,
                shape = MaterialTheme.shapes.large
            ) {
                Icon(
                    imageVector = Icons.Outlined.PowerSettingsNew,
                    contentDescription = null
                )
            }

        }

        VerticalDivider(
            thickness = 0.5.dp
        )

        NavHost(
            navController = navController,
            startDestination = ContactListScreenRoute,
            modifier = Modifier
                .fillMaxSize()
        ) {

            composable<ContactListScreenRoute> {
                ContactListScreen(
                    onUnauthorized = {
                        closeSession()
                    },
                    onAddClick = {
                        navController.navigate(NewContactScreenRoute)
                    },
                    onShowModal = { show ->
                        viewModel.blur(show)
                    }
                )
            }

            composable<NewContactScreenRoute> {
                NewContactScreen(
                    onNavigateBack = {
                        navController.navigateUp()
                    }
                )
            }

            composable<ConfigScreenRoute> {
                ConfigScreen()
            }

        }

    }

    if (uiState.showLogoutDialog) {
        CustomAlertDialog(
            title = "Logout",
            text = "Your session will end.",
            onDismissRequest = viewModel::hideLogoutDialog,
            onNegativeButtonClick = viewModel::hideLogoutDialog,
            onPositiveButtonClick = {
                viewModel.hideLogoutDialog()
                viewModel.logout()
            }
        )
    }

    LaunchedEffect(uiState.logout) {
        if (uiState.logout) {
            closeSession()
        }
    }

    LaunchedEffect(uiState.railItem) {
        navController.navigate(uiState.railItem.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = false
            }
            launchSingleTop = true
            restoreState = true
        }
    }

}

private fun closeSession() {
    Session.clear()
    window.location.reload()
}