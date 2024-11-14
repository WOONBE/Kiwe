package com.kiwe.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class VoiceTempResponse(
    val category: Int,
    val need_temp: Boolean,
    val order: List<OrderTemp>,
    val response: String,
)

@Serializable
data class OrderTemp(
    val menuId: Int,
    val count: Int,
    val options: Option,
    val temperature: String,
)
