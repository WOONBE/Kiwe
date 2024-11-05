package com.kiwe.kiosk.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.kiwe.kiosk.R

val letterSpacing = (-0.03).em
val Typography =
    Typography(
        titleLarge =
            TextStyle(
                fontFamily = FontFamily(Font(R.font.pretendard_extrabold)),
                fontSize = 28.sp,
                letterSpacing = letterSpacing,
            ),
        titleMedium =
            TextStyle(
                fontFamily = FontFamily(Font(R.font.pretendard_extrabold)),
                fontSize = 24.sp,
                letterSpacing = letterSpacing,
            ),
        titleSmall =
            TextStyle(
                fontFamily = FontFamily(Font(R.font.pretendard_extrabold)),
                fontSize = 20.sp,
                letterSpacing = letterSpacing,
            ),
        bodyLarge =
            TextStyle(
                fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                fontSize = 20.sp,
                letterSpacing = letterSpacing,
            ),
        bodyMedium =
            TextStyle(
                fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                fontSize = 18.sp,
                letterSpacing = letterSpacing,
            ),
        bodySmall =
            TextStyle(
                fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                fontSize = 16.sp,
                letterSpacing = letterSpacing,
            ),
        labelLarge =
            TextStyle(
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 16.sp,
                letterSpacing = letterSpacing,
            ),
        labelMedium =
            TextStyle(
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 14.sp,
                letterSpacing = letterSpacing,
            ),
        labelSmall =
            TextStyle(
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 12.sp,
                letterSpacing = letterSpacing,
            ),
    )

val Typography.fixedWidthStyle: TextStyle
    get() =
        TextStyle(
            fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
            fontSize = 16.sp,
            letterSpacing = letterSpacing,
            fontFeatureSettings = "tnum",
        )
