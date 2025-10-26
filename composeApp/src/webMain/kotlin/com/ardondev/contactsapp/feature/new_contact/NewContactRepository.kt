package com.ardondev.contactsapp.feature.new_contact

import com.ardondev.contactsapp.core.Config
import com.ardondev.contactsapp.core.KtorClient
import com.ardondev.contactsapp.core.Session
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.contentType

class NewContactRepository {

    suspend fun addContact(
        name: String,
        phone: String,
        email: String,
        avatar: String? = null
    ): Result<Unit> {
        return try {
            val accessToken = Session.fetchValue(Session.KEY_TOKEN)
                ?: throw Exception("Invalid access token.")

            val request = NewContactRequest(
                name = name,
                phone = phone,
                email = email,
                avatar = avatar
            )

            val response = KtorClient.client.post("${Config.BASE_URL}/rest/v1/contact") {
                contentType(io.ktor.http.ContentType.Application.Json)
                header("apikey", Config.API_KEY)
                header("Prefer", "return=minimal")
                bearerAuth(accessToken)
                setBody(request)
            }

            if (response.status.value !in 200..299) {
                val error: KtorClient.Error = response.body()
                val message = error.msg ?: error.message ?: ""

                if (response.status.value == 401) {
                    throw KtorClient.UnauthorizedException(message)
                } else {
                    throw Exception(message)
                }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}