package com.kiwe.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VoiceOrderRequest(
    val sentence: String,
    val need_temp: Int,
    val order_items: List<OrderList>,
)

@Serializable
data class OrderList(
    @SerialName("menuId")
    val menuId: Int,
    @SerialName("count")
    val count: Int,
    @SerialName("option")
    val option: Option,
)

@Serializable
data class Option(
    @SerialName("shot")
    val shot: Int,
    @SerialName("sugar")
    val sugar: Int,
)
