package com.kiwe.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SignUpResponse(
    val id: Int,
    val name: String,
    val email: String,
    val kioskIds: List<Int>,
    val type: String,
)
