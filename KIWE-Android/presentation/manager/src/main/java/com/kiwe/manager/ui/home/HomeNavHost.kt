package com.kiwe.manager.ui.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kiwe.manager.ui.dashboard.DashBoardScreen
import com.kiwe.manager.ui.kioskmanagement.KioskManagementScreen
import com.kiwe.manager.ui.menumanagement.MenuManagementScreen
import com.kiwe.manager.ui.salesoverview.SalesOverviewScreen

@Composable
fun HomeNavHost(navController: NavHostController) {
    // val navController = rememberNavController()
    NavHost(
        modifier = Modifier.fillMaxWidth(),
        navController = navController,
        startDestination = HomeRoute.DashBoardScreen.route,
    ) {
        composable(route = HomeRoute.DashBoardScreen.route) {
            DashBoardScreen(
//                onNavigateToSignUpScreen = {
//                    navController.navigate(
//                        LoginRoute.SignUpScreen.name,
//                    )
//                },
//                onNavigateToFindPassWordScreen = {
//                    navController.navigate(
//                        LoginRoute.FindPassWordScreen.name,
//                    )
//                },
            )
        }

        composable(route = HomeRoute.MenuManagementScreen.route) {
            MenuManagementScreen()
//            MenuManagementScreen(
//                onNavigateToLoginScreen = {
//                    navController.navigate(
//                        route = LoginRoute.LoginScreen.name,
//                        navOptions =
//                            navOptions {
//                                popUpTo(LoginRoute.LoginScreen.name) { inclusive = true }
//                            },
//                    )
//                },
//            )
        }

        composable(route = HomeRoute.SalesOverviewScreen.route) {
            SalesOverviewScreen()
//                onNavigateToLoginScreen = {
//                    navController.navigate(
//                        route = LoginRoute.LoginScreen.name,
//                        navOptions =
//                            navOptions {
//                                popUpTo(LoginRoute.FindPassWordScreen.name) { inclusive = true }
//                            },
//                    )
//                },
//            )
        }

        composable(route = HomeRoute.KioskManagementScreen.route) {
            KioskManagementScreen()
//                onNavigateToLoginScreen = {
//                    navController.navigate(
//                        route = LoginRoute.LoginScreen.name,
//                        navOptions =
//                        navOptions {
//                            popUpTo(LoginRoute.FindPassWordScreen.name) { inclusive = true }
//                        },
//                    )
//                },
//            )
        }

        composable(route = HomeRoute.SettingScreen.route) {
//            SettingScreen(
//                onNavigateToLoginScreen = {
//                    navController.navigate(
//                        route = LoginRoute.LoginScreen.name,
//                        navOptions =
//                        navOptions {
//                            popUpTo(LoginRoute.FindPassWordScreen.name) { inclusive = true }
//                        },
//                    )
//                },
//            )
        }
    }
}
