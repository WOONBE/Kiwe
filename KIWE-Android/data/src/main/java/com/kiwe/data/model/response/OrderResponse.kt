package com.kiwe.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class OrderResponse(
    val orderId: Int = 0,
    val orderDate: String = "",
    val status: String = "",
    val menuOrders: List<OrderItemResponse> = listOf(),
    val totalPrice: Int = 0,
)

@Serializable
data class OrderItemResponse(
    val menuId: Int,
    val name: String,
    val quantity: Int,
    val price: Int,
)
