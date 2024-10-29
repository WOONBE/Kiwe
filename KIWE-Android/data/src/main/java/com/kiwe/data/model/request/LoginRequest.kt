package com.kiwe.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerialName("id")
    val id: String,
    @SerialName("password")
    val password: String
)
