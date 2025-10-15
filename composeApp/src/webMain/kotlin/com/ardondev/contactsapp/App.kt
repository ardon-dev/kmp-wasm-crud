package com.ardondev.contactsapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ardondev.contactsapp.core.Session
import com.ardondev.contactsapp.feature.contacts.ContactListScreen
import com.ardondev.contactsapp.feature.contacts.ContactsRoute
import com.ardondev.contactsapp.feature.login.LoginRoute
import com.ardondev.contactsapp.feature.login.LoginScreen

@Composable
fun App(
    onNavHostReady: suspend (NavController) -> Unit = {}
) {

    val isAuthenticated = Session.fetchValue(Session.KEY_TOKEN) != null
    println("Authenticated user: $isAuthenticated")

    MaterialTheme {

        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = if (isAuthenticated) ContactsRoute else LoginRoute
        ) {
            composable<LoginRoute> {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(
                            route = ContactsRoute
                        )
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