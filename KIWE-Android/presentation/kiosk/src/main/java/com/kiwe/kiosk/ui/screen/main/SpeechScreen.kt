package com.kiwe.kiosk.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kiwe.kiosk.R
import com.kiwe.kiosk.main.MainViewModel
import com.kiwe.kiosk.ui.screen.main.component.WavyAnimation
import com.kiwe.kiosk.ui.theme.KIWEAndroidTheme
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun SpeechScreen(viewModel: MainViewModel) {
    val state = viewModel.collectAsState().value

    SpeechScreen(
        isDialogOpen = state.isRecording,
        onDismissRequest = viewModel::onDismissSpeechDialog,
        commandText = "\"차가운 아메리카노 한잔 주세요\"",
    )
}

@Composable
private fun SpeechScreen(
    isDialogOpen: Boolean,
    onDismissRequest: () -> Unit,
    commandText: String,
) {
    if (isDialogOpen) {
        Surface(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        brush =
                            Brush.verticalGradient(
                                colors =
                                    listOf(
                                        Color.Black.copy(alpha = 0.2f),
                                        Color.Black.copy(alpha = 0.3f),
                                        Color.Black.copy(alpha = 0.9f),
                                    ),
                            ),
                    ).clickable { onDismissRequest() },
            color = Color.Transparent,
        ) {
            val composition by rememberLottieComposition(
                spec =
                    LottieCompositionSpec.RawRes(
                        R.raw.anim_kiwe_dynamic_recording,
                    ),
            )
            val progress by animateLottieCompositionAsState(
                composition = composition,
                iterations = LottieConstants.IterateForever,
            )

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier =
                        Modifier
                            .fillMaxSize(),
                ) {
                    Spacer(modifier = Modifier.weight(2f))
                    Text(
                        text = commandText,
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
                        modifier = Modifier.padding(horizontal = 8.dp).padding(top = 20.dp),
                    )
                    Box(
                        contentAlignment = Alignment.Center,
                    ) {
                        WavyAnimation(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(240.dp),
                            wavelength = 350f,
                        )
                        WavyAnimation(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(240.dp),
                            wavelength = 200f,
                        )
                        WavyAnimation(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(240.dp),
                            wavelength = 450f,
                        )
//                        LottieAnimation(
//                            composition = composition,
//                            progress = { progress },
//                            modifier = Modifier.size(100.dp),
//                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SpeechScreenPreview() {
    KIWEAndroidTheme {
        SpeechScreen(
            isDialogOpen = true,
            onDismissRequest = {},
            commandText = "dicat",
        )
    }
}
