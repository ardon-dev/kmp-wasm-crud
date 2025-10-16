package com.ardondev.contactsapp.core

import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

object KtorClient {

    private val _client = HttpClient(Js) {
        install(Logging)
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    val client: HttpClient
        get() = _client

    @Serializable
    data class Error(
        @SerialName("code"      ) var code      : Int?    = null,
        @SerialName("error_code") var errorCode : String? = null,
        @SerialName("msg"       ) var msg       : String? = null,
        @SerialName("message"   ) var message   : String? = null,
        @SerialName("hint"      ) var hint      : String? = null
    )

    class UnauthorizedException(override val message: String): Throwable()

}

