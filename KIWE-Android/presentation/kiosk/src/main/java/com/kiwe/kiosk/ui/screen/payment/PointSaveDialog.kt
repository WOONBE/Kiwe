package com.kiwe.kiosk.ui.screen.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kiwe.kiosk.ui.theme.KIWEAndroidTheme
import com.kiwe.kiosk.ui.theme.KiweBrown2
import com.kiwe.kiosk.ui.theme.KiweGreen5
import com.kiwe.kiosk.ui.theme.KiweGray1
import com.kiwe.kiosk.ui.theme.KiweSilver1
import com.kiwe.kiosk.ui.theme.Typography

@Composable
fun PointSaveDialog(modifier: Modifier = Modifier, onDismissRequest: () -> Unit) {
    var phoneNumber by remember { mutableStateOf("010") }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Column(
            modifier = modifier
                .width(300.dp)
                .height(520.dp)
                .background(Color.White, RoundedCornerShape(12.dp))
                .padding(5.dp)
        ) {
            // 다이얼로그 제목
            Text(
                text = "포인트 적립",
                style = Typography.titleMedium.copy(fontSize = 28.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(KiweBrown2)
                    .padding(vertical = 12.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 전화번호 입력 표시
            Text(
                text = formatPhoneNumber(phoneNumber),
                style = Typography.bodyMedium.copy(fontSize = 28.sp),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 숫자 버튼
            Column {
                val rows = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("←", "0", "적립")
                )

                rows.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        row.forEach { label ->
                            KeyButton(
                                label = label,
                                onClick = {
                                    when (label) {
                                        "←" -> phoneNumber =
                                            if (phoneNumber.length > 3) phoneNumber.dropLast(1) else "010"

                                        "적립" -> { /* 적립 버튼 동작 */
                                        }

                                        else -> if (phoneNumber.length < 11) phoneNumber += label
                                    }
                                },
                                isSpecial = label == "적립" || label == "←",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KeyButton(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit,
    isSpecial: Boolean = false
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .clickable { onClick() }
            .background(
                color = when (label) {
                    "←" -> KiweGray1
                    "적립" -> KiweGreen5
                    else -> KiweSilver1
                },
                shape = RoundedCornerShape(4.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 24.sp,
            color = if (label == "적립" || label == "←") Color.White else Color.Black,
            textAlign = TextAlign.Center
        )
    }
}

fun formatPhoneNumber(number: String): String {
    return when (number.length) {
        in 1..3 -> "${number}-"
        in 4..7 -> "${number.substring(0, 3)}-${number.substring(3)}"
        in 8..11 -> "${number.substring(0, 3)}-${number.substring(3, 7)}-${number.substring(7)}"
        else -> number
    }
}

@Preview
@Composable
fun PreviewPointDialogScreen() {
    KIWEAndroidTheme {
        PointSaveDialog(onDismissRequest = {})
    }
}