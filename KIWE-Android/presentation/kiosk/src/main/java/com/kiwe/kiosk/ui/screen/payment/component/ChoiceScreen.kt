package com.kiwe.kiosk.ui.screen.payment.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiwe.kiosk.R
import com.kiwe.kiosk.ui.component.BoldTextWithKeywords
import com.kiwe.kiosk.ui.theme.KIWEAndroidTheme
import com.kiwe.kiosk.ui.theme.KiweGray1
import com.kiwe.kiosk.ui.theme.KiweGreen5
import com.kiwe.kiosk.ui.theme.KiweOrange1
import com.kiwe.kiosk.ui.theme.Typography

@Composable
fun ChoiceScreen(modifier: Modifier = Modifier) {
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
            fullText = "어디에서 이용하시겠습니까?",
            keywords = listOf("어디", "이용"),
            brushFlag = listOf(true, true),
            alignStyle = TextAlign.Center,
            boldStyle = Typography.bodyMedium.copy(fontSize = 24.sp),
            normalStyle = Typography.bodyMedium.copy(fontSize = 24.sp),
            textColor = KiweOrange1,
        )
        Spacer(modifier = Modifier.height(20.dp))
        // 선택 버튼들
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            ChoiceButton(
                label = "포장",
                backgroundColor = KiweGray1,
                iconResourceId = R.drawable.ic_payment_packaging, // 포장 아이콘 리소스 ID 변경 필요
            )
            ChoiceButton(
                label = "매장",
                backgroundColor = KiweGreen5,
                iconResourceId = R.drawable.ic_payment_shop, // 매장 아이콘 리소스 ID 변경 필요
            )
        }
    }
}

@Composable
fun ChoiceButton(
    label: String,
    backgroundColor: Color,
    iconResourceId: Int,
) {
    Surface(
        modifier =
            Modifier
                .size(200.dp)
                .clickable { /* 클릭 이벤트 처리 */ }
                .background(backgroundColor, shape = RoundedCornerShape(16.dp)),
        color = Color.Transparent,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                painter = painterResource(id = iconResourceId),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(48.dp),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                color = Color.White,
                fontSize = 32.sp,
            )
        }
    }
}

@Preview
@Composable
fun ChoiceScreenPreview() {
    KIWEAndroidTheme {
        ChoiceScreen()
    }
}
