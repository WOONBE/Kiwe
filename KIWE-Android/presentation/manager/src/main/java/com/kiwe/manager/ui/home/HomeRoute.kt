package com.kiwe.manager.ui.home

sealed class HomeRoute(
    val name: String,
) {
    object DashBoardScreen : HomeRoute("DashBoardScreen")

    object MenuManagementScreen : HomeRoute("MenuManagementScreen")

    object SalesOverviewScreen : HomeRoute("SalesOverviewScreen")

    object KioskManagementScreen : HomeRoute("KioskManagementScreen")

    object SettingScreen : HomeRoute("SettingScreen")
}
