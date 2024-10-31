package com.kiwe.kiosk.model

data class ShoppingCartItem(
    val menuTitle: String,
    val menuPrice: Int,
    val menuImgUrl: String,
    val count: Int,
    val option: Map<String, String> = mapOf(),
)
