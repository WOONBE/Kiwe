package com.kiwe.kiosk.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val KioskBackground = Color(0xFFF9F9F9)
val KioskBackgroundBrush =
    Brush.verticalGradient(
        colors =
            listOf(
                Color(0xFFFFFFFF),
                Color(0xFFEDE0D4),
                Color(0xFFD2B48C),
            ),
        startY = 0f,
        endY = Float.POSITIVE_INFINITY,
    )
