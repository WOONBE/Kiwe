package com.kiwe.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class OrderResponse(
    val orderId: Int,
    val orderDate: String,
    val status: String,
    val menuOrders: List<MenuOrder>,
    val totalPrice: Int,
    val kioskId: Int,
)
