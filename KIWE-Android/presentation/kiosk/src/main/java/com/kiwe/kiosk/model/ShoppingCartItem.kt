package com.kiwe.kiosk.model

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
