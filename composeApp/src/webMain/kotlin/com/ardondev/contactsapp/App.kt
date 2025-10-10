package com.ardondev.contactsapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ardondev.contactsapp.contact_list.ContactListScreen
import com.ardondev.contactsapp.contact_list.ContactsRoute
import com.ardondev.contactsapp.login.LoginRoute
import com.ardondev.contactsapp.login.LoginScreen

@Composable
fun App(
    onNavHostReady: suspend (NavController) -> Unit = {}
) {

    MaterialTheme {

        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = LoginRoute
        ) {
            composable<LoginRoute> {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(ContactsRoute)
                    }
                )
            }
            composable<ContactsRoute> {
                ContactListScreen()
            }
        }

        LaunchedEffect(navController) {
            onNavHostReady(navController)
        }

    }

}