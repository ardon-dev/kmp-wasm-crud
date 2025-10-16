package com.ardondev.contactsapp.feature.config

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable

@Serializable
object ConfigScreenRoute

@Composable
fun ConfigScreen() {
    Box(Modifier.fillMaxSize().background(Color.Blue))
}