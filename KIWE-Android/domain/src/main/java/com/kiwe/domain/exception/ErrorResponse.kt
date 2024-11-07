package com.kiwe.domain.exception

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val code: Int,
    val message: String,
)
