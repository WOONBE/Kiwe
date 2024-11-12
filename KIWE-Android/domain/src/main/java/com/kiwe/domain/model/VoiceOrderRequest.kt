package com.kiwe.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class VoiceOrderRequest(
    val sentence: String,
    val have_temp: Boolean,
    val order_items: List<OrderList>,
)

@Serializable
data class OrderList(
    val menuId: Int,
    val count: Int,
    val option: Option,
)

@Serializable
data class Option(
    val shot: Boolean,
    val sugar: Boolean,
)
