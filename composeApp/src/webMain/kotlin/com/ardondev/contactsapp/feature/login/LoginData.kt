package com.ardondev.contactsapp.feature.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// UI

data class LoginUiState(
    val email         : String         = "",
    val emailError    : String?        = null,
    val password      : String         = "",
    val passwordError : String?        = null,
    val loading       : Boolean        = false,
    val error         : String?        = null,
    val loginResponse : LoginResponse? = null
)

// API

@Serializable
data class LoginRequest(
    @SerialName("email"   ) var email    : String? = null,
    @SerialName("password") var password : String? = null
)

@Serializable
data class LoginResponse (
    @SerialName("access_token" ) var accessToken  : String? = null,
    @SerialName("token_type"   ) var tokenType    : String? = null,
    @SerialName("expires_in"   ) var expiresIn    : Int?    = null,
    @SerialName("expires_at"   ) var expiresAt    : Int?    = null,
    @SerialName("refresh_token") var refreshToken : String? = null,
    @SerialName("user"         ) var user         : User?   = User(),
    @SerialName("weak_password") var weakPassword : String? = null
) {

    @Serializable
    data class User (
        @SerialName("id"                ) var id               : String?  = null,
        @SerialName("aud"               ) var aud              : String?  = null,
        @SerialName("role"              ) var role             : String?  = null,
        @SerialName("email"             ) var email            : String?  = null,
        @SerialName("email_confirmed_at") var emailConfirmedAt : String?  = null,
        @SerialName("phone"             ) var phone            : String?  = null,
        @SerialName("confirmed_at"      ) var confirmedAt      : String?  = null,
        @SerialName("last_sign_in_at"   ) var lastSignInAt     : String?  = null,
        @SerialName("created_at"        ) var createdAt        : String?  = null,
        @SerialName("updated_at"        ) var updatedAt        : String?  = null,
        @SerialName("is_anonymous"      ) var isAnonymous      : Boolean? = null
    )

}

