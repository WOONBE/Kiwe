package com.kiwe.domain.model

data class Order(
    val menuOrders: List<OrderItem>,
)

fun getDummyOrder(): Order =
    Order(
        menuOrders =
            listOf(
                OrderItem(1, 2),
                OrderItem(2, 3),
                OrderItem(3, 4),
            ),
    )
