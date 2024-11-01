package com.kiwe.kiosk.navigation

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kiwe.kiosk.main.MainViewModel
import com.kiwe.kiosk.ui.screen.intro.IntroScreen
import com.kiwe.kiosk.ui.screen.main.ContainerScreen
import com.kiwe.kiosk.ui.screen.main.SpeechScreen
import com.kiwe.kiosk.ui.screen.menu.MenuScreen
import com.kiwe.kiosk.ui.screen.utils.rotatedScreenSize

@Composable
fun MainNavHost() {
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = viewModel()
    val configuration = LocalConfiguration.current
    val rotationAngle =
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) -90f else 0f

    Surface {
        Scaffold(
            content = { padding ->
                NavHost(
                    modifier =
                        Modifier
                            .rotatedScreenSize(rotationAngle, configuration)
                            .padding(padding),
                    navController = navController,
                    startDestination = MainRoute.MENU.route,
                ) {
                    composable(route = MainRoute.INTRO.route) {
                        ContainerScreen(
                            viewModel = mainViewModel,
                        ) {
                            IntroScreen()
                        }
                    }
                    composable(route = MainRoute.MENU.route) {
                        ContainerScreen(
                            viewModel = mainViewModel,
                        ) {
                            MenuScreen(
                                viewModel = mainViewModel,
                            )
                        }
                    }
                }
                SpeechScreen(
                    viewModel = mainViewModel,
                    rotationAngle = rotationAngle,
                    configuration = configuration,
                )
            },
        )
    }
}
