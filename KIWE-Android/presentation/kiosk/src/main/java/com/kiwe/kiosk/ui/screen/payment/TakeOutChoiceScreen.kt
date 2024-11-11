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
fun TakeOutChoiceScreen(
    modifier: Modifier = Modifier,
    onPackagingClick: () -> Unit = {},
    onStoreClick: () -> Unit = {},
) {
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

@Preview
@Composable
private fun Preview() {
    KIWEAndroidTheme {
        TakeOutChoiceScreen(modifier = Modifier.background(brush = KioskBackgroundBrush))
    }
}
