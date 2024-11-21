package com.kiwe.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Kiosk(
    val id: Int,
    val location: String,
    val status: String,
)
