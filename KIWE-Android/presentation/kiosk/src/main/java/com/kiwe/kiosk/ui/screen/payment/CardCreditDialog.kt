package com.kiwe.kiosk.ui.screen.payment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kiwe.kiosk.R
import com.kiwe.kiosk.ui.component.BoldTextWithKeywords
import com.kiwe.kiosk.ui.theme.KIWEAndroidTheme
import com.kiwe.kiosk.ui.theme.KiweBlack1
import com.kiwe.kiosk.ui.theme.KiweBrown2
import com.kiwe.kiosk.ui.theme.KiweGray1
import com.kiwe.kiosk.ui.theme.KiweOrange1
import com.kiwe.kiosk.ui.theme.KiweSilver1
import com.kiwe.kiosk.ui.theme.KiweWhite1
import com.kiwe.kiosk.ui.theme.Typography
import java.util.Locale

@Composable
fun CardCreditDialog(
    modifier: Modifier = Modifier,
    remainingTime: Long = 0,
    totalAmount: Int = 0,
    cardNumber: String = "",
    onDismissRequest: () -> Unit,
) {
    val priceText = String.format(Locale.KOREAN, "%,d", totalAmount)

    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties =
            DialogProperties(
                dismissOnBackPress = false, // 뒤로가기 눌러도 닫히지 않음
                dismissOnClickOutside = false, // 외부 클릭으로 닫히지 않음
            ),
    ) {
        Column(
            modifier =
                modifier
                    .width(300.dp)
                    .height(520.dp)
                    .background(KiweWhite1, RoundedCornerShape(10.dp))
                    .padding(5.dp),
        ) {
            // 다이얼로그 제목
            Text(
                text = "카드 결제",
                style = Typography.titleMedium.copy(fontSize = 28.sp),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(KiweBrown2, RoundedCornerShape(5.dp))
                        .padding(vertical = 12.dp),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 카드 결제 이미지
            Box(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                    painter = painterResource(id = R.drawable.img_card_credit),
                    contentDescription = null,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            // 남은 시간 표시

            BoldTextWithKeywords(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fullText = "남은 시간: ${30 - remainingTime}초",
                keywords = listOf("${30 - remainingTime}"),
                brushFlag = listOf(true),
                boldStyle = Typography.bodyMedium.copy(fontSize = 14.sp),
                normalStyle = Typography.labelMedium.copy(fontSize = 14.sp, color = KiweGray1),
                textColor = KiweBlack1,
            )
            // 총 결제 금액 표시
            PaymentInfoRow(
                label = "총 결제금액",
                value = {
                    BoldTextWithKeywords(
                        modifier = Modifier,
                        fullText = "$priceText${stringResource(R.string.kiwe_won)}",
                        keywords = listOf(priceText),
                        brushFlag = listOf(true),
                        boldStyle = Typography.bodyMedium.copy(fontSize = 20.sp),
                        normalStyle = Typography.bodyMedium.copy(fontSize = 20.sp),
                        textColor = KiweOrange1,
                    )
                },
                backgroundColor = KiweSilver1,
            )

            // 할부 개월 표시
            PaymentInfoRow(
                label = "할부개월",
                value = {
                    Text(
                        modifier = Modifier,
                        text = "일시불",
                        style = Typography.bodyMedium.copy(fontSize = 20.sp, color = KiweGray1),
                    )
                },
                backgroundColor = KiweWhite1,
            )

            // 카드번호 입력
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(KiweSilver1, RoundedCornerShape(4.dp))
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "카드번호",
                        style = Typography.labelMedium.copy(fontSize = 16.sp),
                        color = KiweBlack1,
                    )

                    Text(
                        text = cardNumber,
                        style = Typography.bodyMedium.copy(fontSize = 16.sp),
                        color = KiweBlack1,
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 취소 버튼 추가
            Button(
                onClick = { onDismissRequest() },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp),
                enabled = cardNumber.isBlank(),
                colors = ButtonDefaults.buttonColors(KiweOrange1),
                shape = RoundedCornerShape(8.dp),
                elevation = ButtonDefaults.buttonElevation(4.dp),
            ) {
                Text(
                    text = "취소",
                    style = Typography.bodyLarge.copy(fontSize = 16.sp, color = KiweWhite1),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
fun PaymentInfoRow(
    label: String,
    value: @Composable () -> Unit = {},
    backgroundColor: Color,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(backgroundColor, RoundedCornerShape(5.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = Typography.labelMedium.copy(fontSize = 16.sp),
            color = KiweBlack1,
        )
        value()
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    KIWEAndroidTheme {
        CardCreditDialog(onDismissRequest = {})
    }
}
