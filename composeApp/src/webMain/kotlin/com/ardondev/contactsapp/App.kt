package com.ardondev.contactsapp

import androidx.compose.runtime.Composable
import com.ardondev.contactsapp.core.Session
import com.ardondev.contactsapp.core.theme.AppTheme
import com.ardondev.contactsapp.feature.login.LoginScreen
import com.ardondev.contactsapp.feature.main.MainScreen
import kotlinx.browser.window

@Composable
fun App() {

    val isAuthenticated = Session.fetchValue(Session.KEY_TOKEN) != null
    println("Authenticated user: $isAuthenticated")

    AppTheme {
        if (isAuthenticated) {
            MainScreen()
        } else {
            LoginScreen(
                onLoginSuccess = {
                    window.location.reload()
                }
            )
        }
    }

}