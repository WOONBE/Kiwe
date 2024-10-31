package com.kiwe.kiosk.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kiwe.kiosk.screen.intro.IntroScreen

@Composable
fun MainNavHost() {
    val navController = rememberNavController()
    Surface {
        Scaffold(
            content = { padding ->
                NavHost(
                    modifier = Modifier.padding(padding),
                    navController = navController,
                    startDestination = MainRoute.INTRO.route,
                ) {
                    composable(route = MainRoute.INTRO.route) {
                        IntroScreen()
                    }
                }
            },
        )
    }
}
