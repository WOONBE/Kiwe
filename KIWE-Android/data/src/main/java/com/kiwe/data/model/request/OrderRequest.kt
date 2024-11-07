package com.kiwe.data.model.request

import com.kiwe.domain.model.OrderItem
import kotlinx.serialization.Serializable

@Serializable
data class OrderRequest(
    val menuOrders: List<OrderItemRequest>,
)

@Serializable
data class OrderItemRequest(
    val menuId: Int,
    val quantity: Int,
)

fun OrderItem.toRequest() =
    OrderItemRequest(
        menuId = menuId,
        quantity = quantity,
    )
