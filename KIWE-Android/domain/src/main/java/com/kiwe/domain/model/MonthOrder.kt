package com.kiwe.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MonthOrder(
    val monthlyOrder: Map<String, Int>,
)
