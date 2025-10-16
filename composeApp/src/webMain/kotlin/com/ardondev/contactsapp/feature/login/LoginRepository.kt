package com.ardondev.contactsapp.feature.login

import com.ardondev.contactsapp.core.Config
import com.ardondev.contactsapp.core.KtorClient
import com.ardondev.contactsapp.core.Session
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
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

    suspend fun refreshToken(): Result<Unit> {
        try {
            val refreshToken = Session.fetchValue(Session.KEY_REFRESH_TOKEN)
                ?: throw Exception("Refresh token not found.")

            val request = RefreshTokenRequest(refreshToken)

            val response = KtorClient.client.post("${Config.BASE_URL}/auth/v1/token") {
                contentType(ContentType.Application.Json)
                parameter("grant_type", "refresh_token")
                header("apikey", Config.API_KEY)
                setBody(request)
            }

            if (response.status.value !in 200..299) {
                val error: KtorClient.Error = response.body()
                throw Exception(error.msg)
            }

            val data: LoginResponse = response.body()

            Session.saveValue(Session.KEY_TOKEN, data.accessToken.orEmpty())
            Session.saveValue(Session.KEY_REFRESH_TOKEN, data.refreshToken.orEmpty())
            Session.saveValue(Session.KEY_EXPIRES_AT, data.expiresAt.toString())

            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun logout(): Result<Unit> {
        try {
            val accessToken = Session.fetchValue(Session.KEY_TOKEN)

            val response = KtorClient.client.post("${Config.BASE_URL}/auth/v1/logout") {
                contentType(ContentType.Application.Json)
                header("apikey", Config.API_KEY)
                bearerAuth(accessToken.orEmpty())
            }

            if (response.status.value !in 200..299) {
                val error: KtorClient.Error = response.body()
                throw Exception(error.msg)
            }

            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

}