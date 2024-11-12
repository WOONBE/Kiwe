package com.kiwe.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class KioskOrder(
    val kioskId: Int,
    val kioskOrderNumber: Int,
)
