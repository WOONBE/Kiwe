package com.kiwe.kiosk.ui.screen.main

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kiwe.kiosk.R
import com.kiwe.kiosk.main.MainViewModel
import com.kiwe.kiosk.ui.screen.utils.rotatedScreenSize
import com.kiwe.kiosk.ui.theme.KIWEAndroidTheme
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun SpeechScreen(
    viewModel: MainViewModel,
    rotationAngle: Float,
    configuration: Configuration,
) {
    val state = viewModel.collectAsState().value

    SpeechScreen(
        rotationAngle = rotationAngle,
        configuration = configuration,
        isDialogOpen = state.isRecording,
        onDismissRequest = viewModel::onDismissSpeechDialog,
        commandText = "\"차가운 아메리카노 한잔 주세요\"",
    )
}

@Composable
private fun SpeechScreen(
    rotationAngle: Float,
    configuration: Configuration,
    isDialogOpen: Boolean,
    onDismissRequest: () -> Unit,
    commandText: String,
) {
    if (isDialogOpen) {
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

        Surface(
            modifier =
                Modifier
                    .rotatedScreenSize(rotationAngle, configuration)
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
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier =
                        Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = commandText,
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 48.sp),
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        contentAlignment = Alignment.Center,
                    ) {
                        LottieAnimation(
                            composition = composition,
                            progress = { progress },
                            modifier = Modifier.size(200.dp).alpha(0.8f),
                        )
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
            0f,
            Configuration(),
            isDialogOpen = true,
            onDismissRequest = {},
            commandText = "dicat",
        )
    }
}
