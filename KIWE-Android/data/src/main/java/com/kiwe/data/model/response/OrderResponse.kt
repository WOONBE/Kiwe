package com.kiwe.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderResponse(
    @SerialName("orderId")
    val orderId: Int = 0,
    @SerialName("orderDate")
    val orderDate: String = "",
    @SerialName("status")
    val status: String = "",
    @SerialName("menuOrders")
    val menuOrders: List<OrderItemResponse> = listOf(),
    @SerialName("totalPrice")
    val totalPrice: Int = 0,
    @SerialName("kioskId")
    val kioskId: Int = 0,
)

@Serializable
data class OrderItemResponse(
    @SerialName("menuId")
    val menuId: Int,
    @SerialName("name")
    val name: String,
    @SerialName("quantity")
    val quantity: Int,
    @SerialName("price")
    val price: Int,
)
