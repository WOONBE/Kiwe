package com.kiwe.kiosk.model

import com.kiwe.domain.model.OrderItem

data class ShoppingCartItem(
    val menuId: Int,
    val menuImgPath: String,
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
        menuId = menuId,
        quantity = count,
    )
