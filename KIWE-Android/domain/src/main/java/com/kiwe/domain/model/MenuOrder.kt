package com.kiwe.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MenuOrder(
    val menuId: Int,
    val name: String,
    val quantity: Int,
    val price: Int,
)
