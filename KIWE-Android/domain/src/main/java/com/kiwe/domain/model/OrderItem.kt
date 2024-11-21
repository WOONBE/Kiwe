package com.kiwe.domain.model

data class OrderItem(
    val menuId: Int,
    val quantity: Int,
)

// TODO : 추후 OrderItem 변경
data class NextOrderItem(
    val menuTitle: String,
    val menuPrice: Int,
    val count: Int,
    val option: Map<String, String> = mapOf(),
)
