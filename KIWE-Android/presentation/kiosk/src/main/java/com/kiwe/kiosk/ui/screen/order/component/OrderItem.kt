package com.kiwe.kiosk.ui.screen.order.component

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.kiwe.kiosk.R
import com.kiwe.kiosk.model.OrderItem

@Composable
fun OrderItem(
    orderItem: OrderItem,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .padding(end = 4.dp)
                .dropShadow(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.Black.copy(alpha = 0.25F),
                    offsetY = 4.dp,
                    offsetX = 4.dp,
                    spread = 0.dp,
                ).clip(RoundedCornerShape(20.dp))
                .background(color = Color.White),
    ) {
        Column(
            modifier =
                Modifier
                    .border(
                        border = BorderStroke(0.dp, Color.Transparent),
                        RoundedCornerShape(20.dp),
                    ).clip(
                        RoundedCornerShape(20.dp),
                    ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                modifier =
                    Modifier
                        .weight(1F),
                model = orderItem.menuImgUrl,
                contentScale = ContentScale.Crop,
                contentDescription = "Translated description of what the image contains",
            )
            Text(
                text = orderItem.menuTitle,
            )
            Text(
                text = "${orderItem.menuPrice}원",
                color = colorResource(R.color.KIWE_orange1),
            )
        }
    }
}

@Preview
@Composable
fun OrderItemPreview() {
    OrderItem(
        OrderItem(
            menuTitle = "디카페인 카페모카",
            menuPrice = 4500,
            menuImgUrl = "https://example.com/image.jpg",
        ),
    )
}

fun Modifier.dropShadow(
    shape: Shape,
    color: Color = Color.Black.copy(1f),
    blur: Dp = 4.dp,
    offsetY: Dp = -40.dp,
    offsetX: Dp = -40.dp,
    spread: Dp = 20.dp,
) = this.drawBehind {
    val shadowSize = Size(size.width + spread.toPx(), size.height + spread.toPx())
    val shadowOutline = shape.createOutline(shadowSize, layoutDirection, this)

    val paint =
        Paint().apply {
            this.color = color
        }

    if (blur.toPx() > 0) {
        paint.asFrameworkPaint().apply {
            maskFilter = BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)
        }
    }

    drawIntoCanvas { canvas ->
        canvas.save()
        canvas.translate(offsetX.toPx(), offsetY.toPx())
        canvas.drawOutline(shadowOutline, paint)
        canvas.restore()
    }
}
