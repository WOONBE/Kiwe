package com.kiwe.kiosk.ui.screen.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiwe.kiosk.R
import com.kiwe.kiosk.ui.component.BoldTextWithKeywords
import com.kiwe.kiosk.ui.screen.payment.component.ChoiceButton
import com.kiwe.kiosk.ui.theme.KIWEAndroidTheme
import com.kiwe.kiosk.ui.theme.KioskBackgroundBrush
import com.kiwe.kiosk.ui.theme.KiweBlack1
import com.kiwe.kiosk.ui.theme.KiweOrange1
import com.kiwe.kiosk.ui.theme.KiweWhite1
import com.kiwe.kiosk.ui.theme.Typography

@Composable
fun PaymentChoiceScreen(
    modifier: Modifier = Modifier,
    onQrClick: () -> Unit = {},
    onCardClick: () -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // 질문 텍스트
        BoldTextWithKeywords(
            modifier = Modifier,
            fullText = "결제 방법을 선택해주세요",
            keywords = listOf("결제", "방법", "선택"),
            brushFlag = listOf(true, true, true),
            alignStyle = TextAlign.Center,
            boldStyle = Typography.bodyMedium.copy(fontSize = 24.sp),
            normalStyle = Typography.bodyMedium.copy(fontSize = 24.sp),
            textColor = KiweOrange1,
        )
        Spacer(modifier = Modifier.height(50.dp))
        // 선택 버튼들
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            ChoiceButton(
                iconResourceId = R.drawable.img_payment_qr,
                isImage = true,
                label = "QR",
                backgroundColor = KiweWhite1,
                labelColor = KiweBlack1,
                onClick = onQrClick,
            )
            ChoiceButton(
                iconResourceId = R.drawable.img_payment_card,
                isImage = true,
                label = "카드",
                backgroundColor = KiweWhite1,
                labelColor = KiweBlack1,
                onClick = onCardClick,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    KIWEAndroidTheme {
        PaymentChoiceScreen(modifier = Modifier.background(brush = KioskBackgroundBrush))
    }
}
