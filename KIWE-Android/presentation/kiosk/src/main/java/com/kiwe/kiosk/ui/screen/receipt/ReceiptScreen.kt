package com.kiwe.kiosk.ui.screen.receipt

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.kiwe.kiosk.ui.component.BoldTextWithKeywords
import com.kiwe.kiosk.ui.theme.KIWEAndroidTheme
import com.kiwe.kiosk.ui.theme.KiweGray1
import com.kiwe.kiosk.ui.theme.KiweOrange1
import com.kiwe.kiosk.ui.theme.KiweSilver1
import com.kiwe.kiosk.ui.theme.Typography
import kotlinx.coroutines.delay

@Composable
fun ReceiptScreen(
    modifier: Modifier = Modifier,
    receiptNumber: String = "",
    onEnterScreen: (Int) -> Unit = {},
) {
    receiptNumber // TODO
    var showReceipt by remember { mutableStateOf(false) }
    val translationY by animateFloatAsState(
        targetValue = if (showReceipt) 0f else -300f,
        animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing),
        label = "",
    )

    LaunchedEffect(Unit) {
        delay(500)
        showReceipt = true
    }

    LaunchedEffect(showReceipt) {
        onEnterScreen(4)
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(200.dp)
                    .background(KiweGray1, shape = RoundedCornerShape(20.dp))
                    .zIndex(1f)
                    .padding(8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                        .background(KiweSilver1, shape = RoundedCornerShape(20.dp)),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    BoldTextWithKeywords(
                        modifier = Modifier,
                        fullText = "결제가 완료되었습니다.",
                        keywords = listOf("결제", "완료"),
                        brushFlag = listOf(true, true),
                        boldStyle = Typography.titleSmall.copy(fontSize = 32.sp),
                        normalStyle = Typography.titleSmall.copy(fontSize = 32.sp),
                        textColor = KiweOrange1,
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    BoldTextWithKeywords(
                        modifier = Modifier,
                        fullText = "영수증 하단\n주문번호를 확인해 주세요",
                        keywords = listOf("주문번호"),
                        brushFlag = listOf(true),
                        boldStyle = Typography.titleSmall.copy(fontSize = 24.sp),
                        normalStyle = Typography.titleSmall.copy(fontSize = 24.sp),
                        alignStyle = TextAlign.Center,
                        textColor = KiweOrange1,
                    )
                }
            }
        }

        Box(
            modifier =
                Modifier
                    .offset(y = translationY.dp)
                    .graphicsLayer {
                        alpha = ((translationY + 300f) / 300f).coerceIn(0f, 1f)
                    }.background(Color.White)
                    .padding(32.dp)
                    .width(200.dp)
                    .height(200.dp)
                    .zIndex(0f),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "2005",
                fontSize = 92.sp,
                style = Typography.bodyLarge.copy(fontSize = 48.sp),
                color = Color.Black,
            )
        }

        Spacer(Modifier.weight(1f))
        Box(
            modifier =
                Modifier
                    .padding(bottom = 24.dp),
        ) {
            Text(
                text = "이용해 주셔서 감사합니다",
                style = Typography.titleLarge,
            )
        }
    }
}

@Preview
@Composable
fun ReceiptScreenPreview() {
    KIWEAndroidTheme {
        ReceiptScreen()
    }
}
