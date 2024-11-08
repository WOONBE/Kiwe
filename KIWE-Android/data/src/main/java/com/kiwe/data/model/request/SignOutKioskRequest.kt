package com.kiwe.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class SignOutKioskRequest(
    val refreshToken: String,
    val password: String,
)
