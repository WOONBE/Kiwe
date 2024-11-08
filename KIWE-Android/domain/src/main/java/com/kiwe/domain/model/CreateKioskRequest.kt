package com.kiwe.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateKioskRequest(
    val location: String,
    val status: String,
    val ownerId: Int,
)
