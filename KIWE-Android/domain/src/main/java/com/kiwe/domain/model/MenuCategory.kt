package com.kiwe.domain.model

import kotlinx.serialization.Serializable

enum class MenuCategory(
    val displayName: String,
    val group: MenuCategoryGroup,
) {
    NEW("신상품", MenuCategoryGroup.NEW),
    COFFEE_ICE("커피(ICE)", MenuCategoryGroup.COFFEE),
    COFFEE_HOT("커피(HOT)", MenuCategoryGroup.COFFEE),
    COLD_BREW("커피(콜드브루)", MenuCategoryGroup.COFFEE),
    DECAF("디카페인", MenuCategoryGroup.DECAF),
    SMOOTHIE_FRAPPE("스무디&프라페", MenuCategoryGroup.SMOOTHIE),
    ADE("에이드", MenuCategoryGroup.DRINKS),
    TEA("티", MenuCategoryGroup.TEA),
    DRINK("음료", MenuCategoryGroup.DRINKS),
    DESSERT("디저트", MenuCategoryGroup.DESSERT),
}

enum class MenuCategoryGroup(
    val displayName: String,
) {
    NEW("신상품"),
    COFFEE("커피"),
    DECAF("디카페인"),
    SMOOTHIE("스무디"),
    TEA("티"),
    DRINKS("음료"),
    DESSERT("디저트"),
}

@Serializable
data class MenuCategoryParam(
    val id: Int,
    val category: String,
    val categoryNumber: Int,
    val hotOrIce: String,
    val name: String,
    val price: Int,
    val description: String,
    val imgPath: String,
)
