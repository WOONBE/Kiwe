package com.kiwe.manager.ui.home

sealed class HomeRoute(
    val route: String,
    val contentDescription: String,
) {
    object DashBoardScreen : HomeRoute("DashBoardScreen", "대시보드 화면")

    object MenuManagementScreen : HomeRoute("MenuManagementScreen", " 메뉴 관리 화면")

    object SalesOverviewScreen : HomeRoute("SalesOverviewScreen", "판매 관리 화면")

    object KioskManagementScreen : HomeRoute("KioskManagementScreen", "키오스크 관리 화면")

    object SettingScreen : HomeRoute("SettingScreen", "세팅 화면")
}
