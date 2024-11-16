package com.kiwe.kiosk.ui.screen.main.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiwe.kiosk.ui.theme.KiweBrown1
import com.kiwe.kiosk.ui.theme.KiweBrown3
import com.kiwe.kiosk.ui.theme.KiweBrown5
import com.kiwe.kiosk.ui.theme.KiweGray1
import com.kiwe.kiosk.ui.theme.KiweSilver1
import com.kiwe.kiosk.ui.theme.KiweWhite1
import com.kiwe.kiosk.ui.theme.Typography

@Composable
fun RoundStepItem(
    title: String,
    isActive: Boolean,
    isFirst: Boolean,
    isLast: Boolean,
    modifier: Modifier = Modifier,
) {
    // 애니메이션을 적용한 색상
    val startColor by animateColorAsState(
        targetValue = if (isActive) KiweBrown3 else KiweSilver1,
        label = "",
    )
    val endColor by animateColorAsState(
        targetValue = if (isActive) KiweBrown5 else KiweBrown1,
        label = "",
    )

    val backgroundColor = Brush.horizontalGradient(listOf(startColor, endColor))

    val textColor by animateColorAsState(
        targetValue = if (isActive) KiweWhite1 else KiweGray1,
        label = "",
    )

    val fontWeight by animateIntAsState(
        targetValue = if (isActive) FontWeight.ExtraBold.weight else FontWeight.Normal.weight,
        label = "",
    )
    val fontSize by animateFloatAsState(targetValue = if (isActive) 20f else 16f, label = "")

    val textStyle =
        Typography.bodyMedium.copy(
            fontSize = fontSize.sp,
            fontWeight = FontWeight(fontWeight),
        )
    val height by animateDpAsState(targetValue = if (isActive) 36.dp else 32.dp, label = "")

    val shape =
        when {
            isFirst -> RoundedCornerShape(topStart = 0.dp, bottomStart = 20.dp)
            isLast -> RoundedCornerShape(topEnd = 20.dp, bottomEnd = 0.dp)
            else -> RoundedCornerShape(0.dp)
        }

    Box(
        modifier =
            modifier
                .background(backgroundColor, shape)
                .fillMaxWidth()
                .height(height)
                .padding(horizontal = 2.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = title,
            color = textColor,
            style = textStyle,
            modifier = Modifier.padding(horizontal = 8.dp),
        )
    }
}
