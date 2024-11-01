package com.kiwe.kiosk.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kiwe.kiosk.main.MainViewModel
import com.kiwe.kiosk.ui.screen.intro.IntroScreen
import com.kiwe.kiosk.ui.screen.main.ContainerScreen
import com.kiwe.kiosk.ui.screen.main.SpeechScreen
import com.kiwe.kiosk.ui.screen.menu.MenuScreen
import com.kiwe.kiosk.ui.screen.order.OrderScreen

@Composable
fun MainNavHost() {
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = viewModel()

    Surface {
        Scaffold(
            content = { padding ->
                ContainerScreen(
                    viewModel = mainViewModel,
                    onBackClick = { navController.navigateUp() },
                ) {
                    NavHost(
                        modifier =
                            Modifier
                                .padding(padding),
                        navController = navController,
                        startDestination = MainRoute.MENU.route,
                        exitTransition = { ExitTransition.None },
                        enterTransition = { EnterTransition.None },
                    ) {
                        composable(route = MainRoute.INTRO.route) {
                            IntroScreen()
                        }
                        composable(route = MainRoute.ORDER.route) {
                            OrderScreen { page ->
                                mainViewModel.setPage(page)
                            }
                        }
                        composable(route = MainRoute.MENU.route) {
                            MenuScreen(
                                viewModel = mainViewModel,
                                onCategoryClick = { category, page ->
                                    navController.navigate(MainRoute.ORDER.route)
                                    mainViewModel.setPage(page)
                                },
                            )
                        }
                    }
                }
            },
        )
        SpeechScreen(
            viewModel = mainViewModel,
        )
    }
}
