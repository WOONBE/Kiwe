package com.kiwe.manager.ui.login

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions

@Composable
fun LoginNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = LoginRoute.LoginScreen.name,
    ) {
        composable(route = LoginRoute.LoginScreen.name) {
            LoginScreen(
                onNavigateToSignUpScreen = {
                    navController.navigate(LoginRoute.SignUpScreen.name)
                },
                onNavigateToFindPassWordScreen = {
                    navController.navigate(LoginRoute.FindPassWordScreen.name)
                },
            )
        }

        composable(route = LoginRoute.SignUpScreen.name) {
            SignUpScreen(
                onNavigateToLoginScreen = {
                    navController.navigate(
                        route = LoginRoute.LoginScreen.name,
                        navOptions =
                            navOptions {
                                popUpTo(LoginRoute.LoginScreen.name)
                            },
                    )
                },
            )
        }

        composable(route = LoginRoute.FindPassWordScreen.name) {
            FindPassWordScreen(
                onNavigateToLoginScreen = {
                    navController.navigate(route = LoginRoute.LoginScreen.name)
                },
            )
        }
    }
}
