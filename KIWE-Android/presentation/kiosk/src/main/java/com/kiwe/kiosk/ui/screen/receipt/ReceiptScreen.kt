package com.kiwe.kiosk.ui.screen.receipt

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
        delay(500) // Delay to start the animation after composable is displayed
        showReceipt = true
    }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color(0xFFE0D6CC)),
        // Beige background color similar to the image
        contentAlignment = Alignment.TopCenter,
    ) {
        // Gray box representing the machine, layered on top of the receipt
        Box(
            modifier =
                Modifier
                    .fillMaxWidth().padding(horizontal = 20.dp)
                    .height(60.dp)
                    .background(Color.Gray)
                    .zIndex(1f) // Ensures this box is layered above the receipt
                    .padding(8.dp),
        )

        Box(
            modifier =
                Modifier
                    .offset(y = translationY.dp)
                    .background(Color.White)
                    .padding(32.dp)
                    .width(200.dp)
                    .height(250.dp) // Adjust size to match receipt size in the image
                    .zIndex(0f),
            // Ensures this is layered below the gray box
            contentAlignment = Alignment.Center,
        ) {
            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = "205",
                fontSize = 80.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
            )
        }

        // Thank you message at the bottom
        Box(
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp),
        ) {
            Text(
                text = "이용해 주셔서 감사합니다",
                style = Typography.titleLarge
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
