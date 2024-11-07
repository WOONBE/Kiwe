package com.kiwe.domain.model

enum class MenuCategory(
    val displayName: String,
) {
    NEW("신상품"),
    COFFEE_ICE("커피(ICE)"),
    COFFEE_HOT("커피(HOT)"),
    COLD_BREW("커피(콜드브루)"),
    DECAF("디카페인"),
    SMOOTHIE_FRAPPE("스무디&프라페"),
    AID("에이드"),
    TEA("티"),
    DRINK("음료"),
    DESSERT("디저트"),
}
