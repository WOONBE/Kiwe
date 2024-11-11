package com.kiwe.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MenuParam(
    val category: String,
    val hotOrIce: String,
    val name: String,
    val price: Int,
    val description: String,
    val imgPath: String,
)
