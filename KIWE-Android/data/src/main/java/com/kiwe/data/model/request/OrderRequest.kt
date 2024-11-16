package com.kiwe.data.model.request

import com.kiwe.domain.model.OrderItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderRequest(
    @SerialName("kioskId")
    val kioskId: Int = 0,
    @SerialName("age")
    val age: Int = 30,
    @SerialName("gender")
    val gender: Int = 1,
    @SerialName("menuOrders")
    val menuOrders: List<OrderItemRequest>,
)

@Serializable
data class OrderItemRequest(
    @SerialName("menuId")
    val menuId: Int,
    @SerialName("quantity")
    val quantity: Int,
)

fun OrderItem.toRequest() =
    OrderItemRequest(
        menuId = menuId,
        quantity = quantity,
    )
