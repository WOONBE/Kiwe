package com.kiwe.kiosk.ui.screen.speech

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiwe.kiosk.R
import com.kiwe.kiosk.main.MainViewModel
import com.kiwe.kiosk.ui.screen.main.MySpeechInputText
import com.kiwe.kiosk.ui.screen.main.component.WavyAnimation
import com.kiwe.kiosk.ui.screen.order.ShoppingCartViewModel
import com.kiwe.kiosk.ui.screen.payment.component.ChoiceButton
import com.kiwe.kiosk.ui.theme.KIWEAndroidTheme
import com.kiwe.kiosk.ui.theme.KiweHOT
import com.kiwe.kiosk.ui.theme.KiweICE
import com.kiwe.kiosk.ui.theme.KiweWhite1
import com.kiwe.kiosk.ui.theme.Typography
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.compose.collectAsState
import timber.log.Timber

const val MAX_SPEECH_WAIT_TIME = 10

private const val TAG = "SpeechScreen"

@Composable
fun SpeechScreen(
    mainViewModel: MainViewModel,
    shoppingCartViewModel: ShoppingCartViewModel,
) {
    val state = mainViewModel.collectAsState().value
//    val context = LocalContext.current
//    val ttsManager = remember { TextToSpeechManager(context) }

    DisposableEffect(Unit) {
        onDispose {
            Timber.tag(TAG).d("onDispose")
        }
    }

    LaunchedEffect(state.voiceResult) {
        if (state.voiceResult.category == 1) { // 이러면 응답이 들어왔다는 의미이며 주문일 때만
            shoppingCartViewModel.onVoiceResult(state.voiceResult)
            mainViewModel.clearVoiceRecord()
        }
    }

    SpeechScreen(
        isOpen = state.isScreenShowing, // 녹음중인 상태일 때 SpeechScreen을 보여준다
        onDismissRequest = mainViewModel::onDismissRequest,
        isMySpeechInputTextOpen = state.isMySpeechInputTextOpen,
        sentence = state.mySpeechText,
        shouldShowRetryMessage = state.shouldShowRetryMessage,
        isTemperatureEmpty = state.isTemperatureEmpty,
    )
}

@Composable
private fun SpeechScreen(
    isOpen: Boolean,
    onDismissRequest: () -> Unit,
    isMySpeechInputTextOpen: Boolean,
    sentence: String = "\"차가운 아메리카노 한잔 주세요\"",
    shouldShowRetryMessage: Boolean,
    isTemperatureEmpty: Boolean,
) {
    if (isOpen) {
        var elapsedTime by remember { mutableLongStateOf(0L) }
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
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Spacer(modifier = Modifier.weight(2f))
                    if (elapsedTime in 0..<MAX_SPEECH_WAIT_TIME) {
                        Text(
                            text = if (shouldShowRetryMessage) "다시 말씀해주세요" else "듣는 중 입니다...",
                            color = Color.White,
                            style =
                                MaterialTheme.typography.titleLarge
                                    .copy(fontSize = 26.sp)
                                    .copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = 8.dp).padding(top = 20.dp),
                        )
                        MySpeechInputText(
                            isMySpeechInputTextOpen = isMySpeechInputTextOpen,
                            sentence = sentence,
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
                    ExampleBox(isMySpeechInputTextOpen, sentence)
                }
                if (isTemperatureEmpty) {
                    elapsedTime = -7 // 7초안에 온도 골라야됨
                    TempBox(isMySpeechInputTextOpen, sentence)
                }
            }
        }
    }
}

@Composable
fun TempBox(
    isMySpeechInputTextOpen: Boolean,
    sentence: String,
    onHotClick: () -> Unit = {},
    onIceClick: () -> Unit = {},
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "온도를 선택해주세요", style = Typography.titleLarge.copy(color = Color.White))
        Row(
            modifier = Modifier.padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            ChoiceButton(
                modifier = Modifier,
                backgroundColor = KiweWhite1,
                iconResourceId = R.drawable.img_hot_drink,
                isImage = true,
                label = "뜨겁게",
                labelColor = KiweHOT,
                onClick = onHotClick,
            )
            ChoiceButton(
                modifier = Modifier,
                backgroundColor = KiweWhite1,
                iconResourceId = R.drawable.img_ice_drink,
                isImage = true,
                label = "차갑게",
                labelColor = KiweICE,
                onClick = onIceClick,
            )
        }
        MySpeechInputText(isMySpeechInputTextOpen, sentence)
    }
}

@Composable
fun ExampleBox(
    isMySpeechInputTextOpen: Boolean,
    sentence: String,
) {
    Column {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.Black.copy(alpha = 0.5f),
                    ).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ExampleItem(text = "예시1", example = "\"차가운 아메리카노 한잔 줘\"")
            ExampleItem(text = "예시2", example = "\"따뜻한 둥글레차 두 잔 줘\"")
            ExampleItem(text = "예시3", example = "\"티라미수 케이크 하나 줘\"")
        }

        MySpeechInputText(
            isMySpeechInputTextOpen = isMySpeechInputTextOpen,
            sentence = sentence,
        )
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
            isOpen = true,
            onDismissRequest = {},
            isMySpeechInputTextOpen = true,
            shouldShowRetryMessage = false,
            sentence = "dfdfdfdfdfdfdfdfd",
            isTemperatureEmpty = true,
        )
    }
}
