package com.kiwe.kiosk.ui.screen.utils

import android.content.res.Configuration
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.rotatedScreenSize(
    rotationAngle: Float,
    configuration: Configuration,
    xOffsetDefault: Dp = 336.dp,
): Modifier {
    val screenWidth =
        if (rotationAngle == -90f) {
            configuration.screenHeightDp.dp
        } else {
            configuration.screenWidthDp.dp
        }
    val screenHeight =
        if (rotationAngle == -90f) {
            configuration.screenWidthDp.dp
        } else {
            configuration.screenHeightDp.dp
        }
    val xOffset = if (rotationAngle == -90f) xOffsetDefault else 0.dp

    return this
        .offset(x = xOffset)
        .requiredWidth(screenWidth)
        .requiredHeight(screenHeight)
        .rotate(rotationAngle)
}
