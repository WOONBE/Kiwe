package com.kiwe.kiosk.ui.screen.order.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kiwe.domain.model.MenuCategoryParam
import com.kiwe.kiosk.BuildConfig.BASE_IMAGE_URL
import com.kiwe.kiosk.R
import com.kiwe.kiosk.ui.theme.KiweWhite1
import com.kiwe.kiosk.ui.theme.Typography
import com.kiwe.kiosk.utils.dropShadow
import timber.log.Timber
import java.util.Locale

@Composable
fun OrderItem(
    orderItem: MenuCategoryParam,
    modifier: Modifier = Modifier,
    onClick: (Int, String, String, String, Int, Offset) -> Unit,
) {
    var cartPosition by remember { mutableStateOf(Offset.Zero) }
    Box(
        modifier =
            modifier
                .padding(end = 4.dp)
                .dropShadow(
                    shape = RoundedCornerShape(5.dp),
                    color = Color.Black.copy(alpha = 0.25F),
                    offsetY = 4.dp,
                    offsetX = 4.dp,
                    spread = 0.dp,
                ).clip(RoundedCornerShape(5.dp))
                .background(color = KiweWhite1)
                .clickable {
                    onClick(
                        orderItem.id,
                        orderItem.imgPath,
                        orderItem.name,
                        orderItem.description,
                        orderItem.price,
                        cartPosition,
                    )
                    Timber.tag("OrderItem클릭시").d("description : ${orderItem.description}")
                }.onGloballyPositioned {
                    cartPosition = Offset(it.positionInRoot().x, it.positionInRoot().y - it.size.height)
                },
    ) {
        Column(
            modifier =
                Modifier
                    .padding(1.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
//            Log.d(TAG, "https://" + BASE_IMAGE_URL + URLEncoder.encode(orderItem.imgPath, StandardCharsets.UTF_8.toString()))
//            Log.d(TAG, "OrderItem: ${"https://" + BASE_IMAGE_URL + orderItem.imgPath}")

            AsyncImage(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(bottom = 2.dp)
                        .background(KiweWhite1),
                model = "https://" + BASE_IMAGE_URL + orderItem.imgPath,
                contentScale = ContentScale.Crop,
                contentDescription = "Translated description of what the image contains",
            )
            val title: String =
                if (orderItem.hotOrIce.isNotEmpty()) {
                    "[" + orderItem.hotOrIce + "] " + orderItem.name
                } else {
                    orderItem.name
                }
            val fontSizeSp = 8.sp
            val lineHeight = fontSizeSp.value * 1.2f // 여유 높이 1.7배로 설정 (필요에 따라 조절)
            val maxLines = 2
            // sp 값을 dp 값으로 변환
            val boxHeightDp = with(LocalDensity.current) { (lineHeight * maxLines).toDp() }
            Box(
                modifier =
                    Modifier
                        .height(boxHeightDp + 10.dp) // 두 줄 높이에 맞게 고정
                        .fillMaxWidth(),
                // 텍스트가 가로로도 중앙에 위치하도록
                contentAlignment = Alignment.Center, // 수직 및 수평 중앙 정렬
            ) {
                Text(
                    text = title,
                    style =
                        Typography.bodySmall.copy(
                            fontSize = fontSizeSp,
                            lineHeight = fontSizeSp * 1.0,
                        ),
                    textAlign = TextAlign.Center,
                    maxLines = maxLines, // 두 줄까지만 표시
                )
            }
            Text(
                text = String.format(Locale.getDefault(), "%,d원", orderItem.price),
                color = colorResource(R.color.KIWE_orange1),
                style = Typography.bodySmall.copy(fontSize = 8.sp),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview
@Composable
fun OrderItemPreview() {
    OrderItem(
        MenuCategoryParam(
            id = 1,
            category = "디카페인",
            categoryNumber = 1,
            hotOrIce = "HOT",
            name = "디카페인 아메리카노",
            price = 2500,
            description = "향과 풍미 그대로 카페인만을 낮춰 민감한 분들도 안심하고 매일매일 즐길 수 있는 디카페인 커피",
            imgPath = "drinks/HOT_디카페인 아메리카노.jpg",
        ),
        onClick = { _, _, _, _, _, _ -> },
    )
}
