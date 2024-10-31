package com.kiwe.kiosk.navigation

enum class MainRoute(
    val route: String,
    val contentDescription: String,
) {
    INTRO(route = "IntroScreen", contentDescription = "시작화면"),
    CATEGORY(route = "CategoryScreen", contentDescription = "카테고리"),
    MENU(route = "MenuScreen", contentDescription = "메뉴"),
}
