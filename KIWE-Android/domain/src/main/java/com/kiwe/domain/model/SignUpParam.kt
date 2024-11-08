package com.kiwe.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SignUpParam(
    val name: String,
    val email: String,
    val password: String,
    val kioskIds: List<Int>,
)
