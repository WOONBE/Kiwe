package com.kiwe.kiosk.ui.screen.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kiwe.kiosk.R
import com.kiwe.kiosk.main.MainViewModel
import com.kiwe.kiosk.ui.component.BoldTextWithKeywords
import com.kiwe.kiosk.ui.screen.main.MySpeechInputText
import com.kiwe.kiosk.ui.screen.payment.component.ChoiceButton
import com.kiwe.kiosk.ui.theme.KIWEAndroidTheme
import com.kiwe.kiosk.ui.theme.KioskBackgroundBrush
import com.kiwe.kiosk.ui.theme.KiweBlack1
import com.kiwe.kiosk.ui.theme.KiweOrange1
import com.kiwe.kiosk.ui.theme.KiweWhite1
import com.kiwe.kiosk.ui.theme.Typography
import org.orbitmvi.orbit.compose.collectAsState
import timber.log.Timber

private const val TAG = "TakeOutChoiceScreen"

@Composable
fun TakeOutChoiceScreen(
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier,
    onPackagingClick: () -> Unit = {},
    onStoreClick: () -> Unit = {},
) {
    val state = mainViewModel.collectAsState().value
    var isVoiceModeOn by remember { mutableStateOf(false) }
    LaunchedEffect(state.voiceShoppingCart, state.isPayment) {
        if (state.voiceShoppingCart.isNotEmpty() && !state.isPayment) {
            // 음성모드가 진행중이라는 말
            isVoiceModeOn = true
            Timber.tag(TAG).d("LaunchedEffect")
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            Timber.tag(TAG).d("DisposableEffect")
            mainViewModel.clearPaymentProcess()
        }
    }

    LaunchedEffect(state.isPayment) {
        if (isVoiceModeOn && state.isPayment) {
            onPackagingClick()
            isVoiceModeOn = false // 창 닫기
        }
    }

    TakeOutChoiceScreen(
        modifier = modifier,
        onPackagingClick = onPackagingClick,
        onStoreClick = onStoreClick,
        isVoiceModeOn = isVoiceModeOn,
    )

    PayQueryStateBox(
        isPayment = isVoiceModeOn,
        page = state.page,
        onClose = {
            isVoiceModeOn = false
        },
        onPackagingClick = onPackagingClick,
        onStoreClick = onStoreClick,
        isMySpeechInputTextOpen = state.isMySpeechInputTextOpen,
        sentence = state.mySpeechText,
    )
}

@Composable
private fun TakeOutChoiceScreen(
    modifier: Modifier = Modifier,
    onPackagingClick: () -> Unit = {},
    onStoreClick: () -> Unit = {},
    isVoiceModeOn: Boolean = false,
) {
    if (!isVoiceModeOn) {
        Column(
            modifier =
                modifier
                    .fillMaxSize()
                    .padding(5.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // 질문 텍스트

            BoldTextWithKeywords(
                modifier = Modifier,
                fullText = "어디에서 이용하시겠습니까?",
                keywords = listOf("어디", "이용"),
                brushFlag = listOf(true, true),
                alignStyle = TextAlign.Center,
                boldStyle = Typography.bodyMedium.copy(fontSize = 24.sp),
                normalStyle = Typography.bodyMedium.copy(fontSize = 24.sp),
                textColor = KiweOrange1,
            )

            // 선택 버튼들
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                ChoiceButton(
                    iconResourceId = R.drawable.img_use_takeout,
                    isImage = true,
                    label = "포장",
                    backgroundColor = KiweWhite1,
                    labelColor = KiweBlack1,
                    onClick = onPackagingClick,
                )
                ChoiceButton(
                    iconResourceId = R.drawable.img_use_store,
                    isImage = true,
                    label = "매장",
                    backgroundColor = KiweWhite1,
                    labelColor = KiweBlack1,
                    onClick = onStoreClick,
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun PayQueryStateBox(
    isPayment: Boolean,
    page: Int = 0,
    isMySpeechInputTextOpen: Boolean,
    sentence: String,
    onClose: () -> Unit,
    onPackagingClick: () -> Unit = {},
    onStoreClick: () -> Unit = {},
) {
    if (isPayment) {
        Timber.tag(TAG).d("QueryStateBox $page")
        Dialog(onDismissRequest = {
            onClose()
        }) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "어디에서 이용하시겠습니까?",
                        style = Typography.titleLarge.copy(color = Color.White),
                    )
                    Text(
                        text = "음성으로 하셔도 됩니다",
                        style = Typography.titleMedium.copy(color = Color.White),
                    )
                    // 선택 버튼들
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        ChoiceButton(
                            iconResourceId = R.drawable.img_use_takeout,
                            isImage = true,
                            label = "포장",
                            backgroundColor = KiweWhite1,
                            labelColor = KiweBlack1,
                            onClick = onPackagingClick,
                        )
                        ChoiceButton(
                            iconResourceId = R.drawable.img_use_store,
                            isImage = true,
                            label = "매장",
                            backgroundColor = KiweWhite1,
                            labelColor = KiweBlack1,
                            onClick = onStoreClick,
                        )
                    }
                    MySpeechInputText(
                        isMySpeechInputTextOpen = isMySpeechInputTextOpen,
                        sentence = sentence,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    KIWEAndroidTheme {
        TakeOutChoiceScreen(
            modifier = Modifier.background(brush = KioskBackgroundBrush),
            isVoiceModeOn = true,
        )
    }
}

@Preview
@Composable
private fun Preview2() {
    KIWEAndroidTheme {
        PayQueryStateBox(
            isPayment = true,
            page = 1,
            onClose = {},
            isMySpeechInputTextOpen = true,
            sentence = "테스트",
        )
    }
}
