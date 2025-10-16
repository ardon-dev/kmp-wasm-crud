package com.ardondev.contactsapp.feature.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Contacts
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ardondev.contactsapp.core.components.CustomNavigationRailItem
import com.ardondev.contactsapp.feature.config.ConfigScreen
import com.ardondev.contactsapp.feature.config.ConfigScreenRoute
import com.ardondev.contactsapp.feature.contact_list.ContactListScreen
import com.ardondev.contactsapp.feature.contact_list.ContactListScreenRoute

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
            header = { Box {} },
            containerColor = MaterialTheme.colorScheme.primary
        ) {

            CustomNavigationRailItem(
                selected = uiState.railItem == RailItem.Contacts,
                label = RailItem.Contacts.label,
                onClick = {
                    viewModel.updateRailItem(RailItem.Contacts)
                },
                defaultIcon = Icons.Outlined.Contacts,
                selectedIcon = Icons.Filled.Contacts
            )

            CustomNavigationRailItem(
                selected = uiState.railItem == RailItem.Config,
                label = RailItem.Config.label,
                onClick = {
                    viewModel.updateRailItem(RailItem.Config)
                },
                defaultIcon = Icons.Outlined.Settings,
                selectedIcon = Icons.Filled.Settings
            )

        }

        NavHost(
            navController = navController,
            startDestination = ContactListScreenRoute,
            modifier = Modifier
                .fillMaxSize()
        ) {

            composable<ContactListScreenRoute> {
                ContactListScreen()
            }

            composable<ConfigScreenRoute> {
                ConfigScreen()
            }

        }

    }

    LaunchedEffect(uiState.railItem) {
        navController.navigate(uiState.railItem.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

}