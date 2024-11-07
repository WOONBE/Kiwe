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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.kiwe.kiosk.ui.theme.KIWEAndroidTheme
import com.kiwe.kiosk.ui.theme.Typography
import kotlinx.coroutines.delay

@Composable
fun ReceiptScreen() {
    var showReceipt by remember { mutableStateOf(false) }
    val translationY by animateFloatAsState(
        targetValue = if (showReceipt) 0f else -300f,
        animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing),
    )

    LaunchedEffect(Unit) {
        delay(500)
        showReceipt = true
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(200.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(20.dp))
                    .zIndex(1f)
                    .padding(8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text =
                        buildAnnotatedString {
                            withStyle(
                                style =
                                    SpanStyle(
                                        color = Color(0xFFD35400),
                                        fontWeight = FontWeight.Bold,
                                    ),
                            ) {
                                append("결제")
                            }
                            withStyle(
                                style =
                                    SpanStyle(
                                        color = Color.Black,
                                        fontWeight = FontWeight.Normal,
                                    ),
                            ) {
                                append("가 ")
                            }
                            withStyle(
                                style =
                                    SpanStyle(
                                        color = Color(0xFFD35400),
                                        fontWeight = FontWeight.Bold,
                                    ),
                            ) {
                                append("완료")
                            }
                            withStyle(
                                style =
                                    SpanStyle(
                                        color = Color.Black,
                                        fontWeight = FontWeight.Normal,
                                    ),
                            ) {
                                append("되었습니다")
                            }
                        },
                    fontSize = 48.sp,
                    style = Typography.titleLarge,
                    modifier = Modifier.padding(16.dp),
                )
                Text(
                    text =
                        buildAnnotatedString {
                            withStyle(
                                style =
                                    SpanStyle(
                                        color = Color.Black,
                                        fontWeight = FontWeight.Normal,
                                    ),
                            ) {
                                append("영수증 하단 ")
                            }
                            withStyle(
                                style =
                                    SpanStyle(
                                        color = Color(0xFFD35400),
                                        fontWeight = FontWeight.Bold,
                                    ),
                            ) {
                                append("주문번호")
                            }
                            withStyle(
                                style =
                                    SpanStyle(
                                        color = Color.Black,
                                        fontWeight = FontWeight.Normal,
                                    ),
                            ) {
                                append("를 확인해 주세요")
                            }
                        },
                    fontSize = 32.sp,
                    style = Typography.titleLarge,
                    modifier = Modifier.padding(16.dp),
                )
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
                text = "205",
                fontSize = 92.sp,
                style = Typography.titleLarge,
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
