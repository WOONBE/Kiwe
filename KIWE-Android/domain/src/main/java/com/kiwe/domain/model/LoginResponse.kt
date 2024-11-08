package com.kiwe.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val email: String,
    val type: String,
    val accessToken: String,
    val refreshToken: String,
)
