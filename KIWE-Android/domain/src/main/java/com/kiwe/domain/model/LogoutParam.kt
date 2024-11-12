package com.kiwe.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LogoutParam(
    val refreshToken: String,
)
