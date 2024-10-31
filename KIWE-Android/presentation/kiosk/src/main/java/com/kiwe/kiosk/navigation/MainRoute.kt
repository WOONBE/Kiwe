package com.kiwe.kiosk.navigation

enum class MainRoute(
    val route: String,
    val contentDescription: String,
) {
    INTRO(route = "IntroScreen", contentDescription = "시작화면"),
    ORDER(route = "OrderScreen", contentDescription = "주문선택"),
    MENU(route = "MenuScreen", contentDescription = "메뉴"),
}
