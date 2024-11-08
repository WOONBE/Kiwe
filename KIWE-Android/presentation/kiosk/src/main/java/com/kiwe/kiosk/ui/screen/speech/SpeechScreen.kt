package com.kiwe.kiosk.ui.screen.speech

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiwe.kiosk.main.MainViewModel
import com.kiwe.kiosk.ui.screen.main.component.WavyAnimation
import com.kiwe.kiosk.ui.screen.utils.TextToSpeechManager
import com.kiwe.kiosk.ui.theme.KIWEAndroidTheme
import com.kiwe.kiosk.ui.theme.Typography
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.compose.collectAsState
import timber.log.Timber

const val MAX_SPEECH_WAIT_TIME = 5

private const val TAG = "SpeechScreen"

@Composable
fun SpeechScreen(viewModel: MainViewModel) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current
    val ttsManager = remember { TextToSpeechManager(context) }

    DisposableEffect(Unit) {
        onDispose {
            Timber.tag(TAG).d("onDispose")
            ttsManager.stop()
        }
    }

    SpeechScreen(
        isDialogOpen = state.isDialogShowing, // 녹음중인 상태일 때 SpeechScreen을 보여준다
        onResult = viewModel::onSpeechResult,
        onDismissRequest = viewModel::onDismissRequest,
        recognizedText = state.recognizedText,
        commandText = "\"차가운 아메리카노 한잔 주세요\"",
        shouldShowRetryMessage = state.shouldShowRetryMessage,
    )
}

@Composable
private fun SpeechScreen(
    isDialogOpen: Boolean,
    onResult: (String) -> Unit,
    onDismissRequest: () -> Unit,
    recognizedText: String,
    commandText: String,
    shouldShowRetryMessage: Boolean,
) {
    if (isDialogOpen) {
        var elapsedTime by remember { mutableLongStateOf(0L) }
        onResult // TODO
        LaunchedEffect(Unit) {
            delay(1000)
            elapsedTime = 0L // 타이머 초기화
            while (true) {
                delay(1000)
                elapsedTime += 1
                if (elapsedTime >= MAX_SPEECH_WAIT_TIME) break
            }
        }

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
//            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.anim_kiwe_dynamic_recording))
//            val progress by animateLottieCompositionAsState(
//                composition = composition,
//                iterations = LottieConstants.IterateForever,
//            )

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Spacer(modifier = Modifier.weight(2f))
                    if (elapsedTime < MAX_SPEECH_WAIT_TIME) {
                        Text(
                            text = if (shouldShowRetryMessage) "다시 말씀해주세요" else "듣는 중 입니다...",
                            color = Color.White,
                            style =
                                MaterialTheme.typography.titleLarge
                                    .copy(fontSize = 26.sp)
                                    .copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = 8.dp).padding(top = 20.dp),
                        )
                        Text(
                            text = recognizedText.ifEmpty { commandText },
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
                            modifier = Modifier.padding(horizontal = 8.dp).padding(top = 8.dp),
                        )
                    }

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
                    }
                }
                if (elapsedTime >= MAX_SPEECH_WAIT_TIME) {
                    ExampleBox()
                }
            }
        }
    }
}

@Composable
fun ExampleBox() {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(shape = RoundedCornerShape(20.dp), color = Color.Black.copy(alpha = 0.5f))
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ExampleItem(text = "예시1", example = "\"차가운 아메리카노 한잔 줘\"")
        ExampleItem(text = "예시2", example = "\"따뜻한 둥글레차 두 잔 줘\"")
        ExampleItem(text = "예시3", example = "\"티라미수 케이크 하나 줘\"")
    }
}

@Composable
fun ExampleItem(
    text: String,
    example: String,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.Black.copy(alpha = 0.3f),
                ).padding(horizontal = 12.dp)
                .padding(vertical = 8.dp),
    ) {
        Text(
            text = text,
            color = Color.White,
            style = Typography.bodyMedium,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = example,
            color = Color.White,
            style = Typography.bodyMedium,
        )
    }
}

@Preview
@Composable
fun SpeechScreenPreview() {
    KIWEAndroidTheme {
        SpeechScreen(
            isDialogOpen = true,
            onResult = {},
            onDismissRequest = {},
            recognizedText = "",
            shouldShowRetryMessage = false,
            commandText = "dicat",
        )
    }
}
