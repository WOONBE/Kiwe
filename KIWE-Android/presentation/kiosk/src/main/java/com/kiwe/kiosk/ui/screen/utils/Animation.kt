package com.kiwe.kiosk.ui.screen.utils

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.kiwe.kiosk.BuildConfig.BASE_IMAGE_URL
import kotlinx.coroutines.launch

@Composable
fun Animation(
    onFinish: () -> Unit,
    imageUrl: String,
    target: Offset,
    startOffset: Offset,
) {
    val scale = remember { Animatable(1f) }
    val offsetX = remember { Animatable(startOffset.x) }
    val offsetY = remember { Animatable(startOffset.y) }
    LaunchedEffect(Unit) {
        launch {
            scale.animateTo(
                targetValue = 0.2f,
                animationSpec =
                    tween(
                        durationMillis = 2000,
                        easing = EaseIn,
                    ),
            )
        }
        launch {
            offsetX.animateTo(
                target.x,
                animationSpec =
                    tween(
                        durationMillis = 2000,
                        easing = EaseIn,
                    ),
            ) // 이동할 X 좌표 (대략적인 예시)
            onFinish()
        }
        launch {
            offsetY.animateTo(
                target.y,
                animationSpec =
                    tween(
                        durationMillis = 2000,
                        easing = EaseIn,
                    ),
            ) // 이동할 Y 좌표 (대략적인 예시)
        }
    }

    AsyncImage(
        model = "https://$BASE_IMAGE_URL$imageUrl",
        contentDescription = null,
        modifier =
            Modifier
                .size(100.dp)
                .zIndex(10F)
                .graphicsLayer(
                    scaleX = scale.value,
                    scaleY = scale.value,
                    translationX = offsetX.value,
                    translationY = offsetY.value,
                ),
    )
}
