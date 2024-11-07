package com.kiwe.kiosk.model

import com.kiwe.domain.model.OrderItem

data class ShoppingCartItem(
    val menuTitle: String,
    val menuRadioOption: MutableMap<String, Pair<String, Int>> = mutableMapOf(),
    val defaultPrice: Int = 0,
    var count: Int,
) {
    val totalPrice
        get() =
            defaultPrice +
                menuRadioOption.values.sumOf {
                    it.second
                }
}

fun ShoppingCartItem.toOrderItem(): OrderItem =
    OrderItem(
        menuId = menuTitle.length,
        quantity = count,
    )
