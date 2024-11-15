package com.kiwe.kiosk.model

data class OrderOption(
    val optionImgUrl: String?,
    val optionImgRes: Int?,
    val title: String,
    val price: Int,
    val radio: Boolean,
)
