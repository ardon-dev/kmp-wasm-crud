package com.ardondev.contactsapp.feature.contact_list

import com.ardondev.contactsapp.core.Config
import com.ardondev.contactsapp.core.KtorClient
import com.ardondev.contactsapp.core.Session
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ContactListRepository {

    suspend fun getContacts(): Result<List<Contact>> {
        return try {
            val accessToken = Session.fetchValue(Session.KEY_TOKEN)

            if (accessToken == null) {
                throw Exception("Invalid access token.")
            }

            val response = KtorClient.client.get("${Config.BASE_URL}/rest/v1/contact") {
                contentType(ContentType.Application.Json)
                parameter("select", "*")
                header("apikey", Config.API_KEY)
                bearerAuth(accessToken)
            }

            if (response.status.value !in 200..299) {
                val error: KtorClient.Error = response.body()
                val message = error.msg ?: error.message ?: ""

                if (response.status.value == 401) {
                    throw KtorClient.UnauthorizedException(message)
                } else {
                    throw KtorClient.UnauthorizedException(message)
                }
            }

            val data: List<Contact> = response.body()
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}