package com.ardondev.contactsapp.feature.login

import com.ardondev.contactsapp.core.Config
import com.ardondev.contactsapp.core.KtorClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType


class LoginRepository() {

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val request = LoginRequest(email, password)

            val response = KtorClient.client.post("${Config.BASE_URL}/auth/v1/token") {
                contentType(ContentType.Application.Json)
                parameter("grant_type", "password")
                header("apikey", Config.API_KEY)
                setBody(request)
            }

            if (response.status.value !in 200..299) {
                val error: KtorClient.Error = response.body()
                throw Exception(error.msg)
            }

            val data: LoginResponse = response.body()
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}