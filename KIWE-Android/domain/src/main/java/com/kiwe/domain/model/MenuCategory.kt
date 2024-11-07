package com.kiwe.domain.model

data class MenuCategory(
    val id: Int,
    val category: String,
    val categoryNumber: Int,
    val hotOrIce: String,
    val name: String,
    val price: Int,
    val description: String,
    val imgPath: String,
)
