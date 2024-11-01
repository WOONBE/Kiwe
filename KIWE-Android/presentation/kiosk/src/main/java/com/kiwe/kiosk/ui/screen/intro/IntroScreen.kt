package com.kiwe.kiosk.ui.screen.intro

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiwe.kiosk.main.MainSideEffect
import com.kiwe.kiosk.main.MainViewModel
import com.kiwe.kiosk.ui.theme.KIWEAndroidTheme
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun IntroScreen(viewModel: MainViewModel = hiltViewModel()) {
//    val state = viewModel.collectAsState().value
    viewModel.collectSideEffect {
        when (it) {
            MainSideEffect.NavigateToNextScreen -> TODO()
            is MainSideEffect.Toast -> TODO()
        }
    }
    IntroScreen()
}

@Composable
private fun IntroScreen() {
    Text(modifier = Modifier.fillMaxSize(), text = "IntroScreen")
}

@Preview
@Composable
fun IntroScreenPreview() {
    KIWEAndroidTheme {
        Surface {
            IntroScreen()
        }
    }
}
