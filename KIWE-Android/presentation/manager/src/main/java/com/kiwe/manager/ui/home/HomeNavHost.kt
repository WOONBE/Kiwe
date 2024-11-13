package com.kiwe.manager.ui.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kiwe.manager.ui.dashboard.DashBoardScreen

@Composable
fun HomeNavHost() {
    val navController = rememberNavController()
    NavHost(
        modifier = Modifier.fillMaxWidth(),
        navController = navController,
        startDestination = HomeRoute.DashBoardScreen.name,
    ) {
        composable(route = HomeRoute.DashBoardScreen.name) {
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

        composable(route = HomeRoute.MenuManagementScreen.name) {
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

        composable(route = HomeRoute.SalesOverviewScreen.name) {
//            SalesOverviewScreen(
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

        composable(route = HomeRoute.KioskManagementScreen.name) {
//            KioskManagementScreen(
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

        composable(route = HomeRoute.SettingScreen.name) {
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
