package com.ardondev.contactsapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform