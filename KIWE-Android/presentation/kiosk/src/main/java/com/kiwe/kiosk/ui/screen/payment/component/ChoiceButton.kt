package com.kiwe.kiosk.ui.screen.payment.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiwe.kiosk.ui.theme.Typography

/**
 * 선택지를 버튼으로 제공
 *
 * @param modifier
 * @param backgroundColor 배경색
 * @param iconResourceId 상단 아이콘
 * @param label 하단 텍스트
 * @param onClick 클릭 이벤트
 */
@Composable
fun ChoiceButton(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    iconResourceId: Int,
    isImage: Boolean = false,
    label: String,
    labelColor: Color,
    onClick: () -> Unit = {},
) {
    Card(
        modifier =
            modifier
                .size(150.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp), // elevation 설정
        colors = CardDefaults.cardColors(containerColor = backgroundColor), // 백그라운드 색상 설정
        onClick = onClick,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (isImage) {
                Image(
                    painter = painterResource(id = iconResourceId),
                    contentDescription = null,
                    modifier =
                        Modifier
                            .size(64.dp),
                )
            } else {
                Icon(
                    painter = painterResource(id = iconResourceId),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(64.dp),
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier,
                text = label,
                color = labelColor,
                style = Typography.bodyMedium.copy(fontSize = 32.sp),
            )
        }
    }
}
