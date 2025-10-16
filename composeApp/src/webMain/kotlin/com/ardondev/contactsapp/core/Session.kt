package com.ardondev.contactsapp.core

import kotlinx.browser.window

object Session {

    const val KEY_TOKEN = "token"
    const val KEY_REFRESH_TOKEN = "refresh_token"
    const val KEY_EXPIRES_AT = "expires_at"

    fun saveValue(key: String, value: String) {
        window.sessionStorage.setItem(key, value)
    }

    fun fetchValue(key: String): String? {
        return window.sessionStorage.getItem(key)
    }

    fun clear() {
        window.sessionStorage.clear()
    }

}

