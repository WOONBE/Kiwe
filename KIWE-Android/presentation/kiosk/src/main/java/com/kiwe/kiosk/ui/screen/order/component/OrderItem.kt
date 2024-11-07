package com.kiwe.kiosk.ui.screen.order.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.kiwe.domain.model.MenuCategoryParam
import com.kiwe.kiosk.BuildConfig.BASE_IMAGE_URL
import com.kiwe.kiosk.R
import com.kiwe.kiosk.ui.theme.Typography
import com.kiwe.kiosk.utils.dropShadow
import java.util.Locale

@Composable
fun OrderItem(
    orderItem: MenuCategoryParam,
    modifier: Modifier = Modifier,
    onClick: (String, Int) -> Unit,
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
                .background(color = Color.White)
                .clickable { onClick(orderItem.name, orderItem.price) },
    ) {
        Column(
            modifier =
                Modifier
                    .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
//            Log.d(TAG, "https://" + BASE_IMAGE_URL + URLEncoder.encode(orderItem.imgPath, StandardCharsets.UTF_8.toString()))
//            Log.d(TAG, "OrderItem: ${"https://" + BASE_IMAGE_URL + orderItem.imgPath}")

            AsyncImage(
                modifier =
                    Modifier
                        .weight(1F)
                        .padding(bottom = 10.dp),
                model = "https://" + BASE_IMAGE_URL + orderItem.imgPath,
                contentScale = ContentScale.Crop,
                contentDescription = "Translated description of what the image contains",
            )
            Text(
                text = "[" + orderItem.hotOrIce + "]\n" + orderItem.name,
                style = Typography.bodySmall,
                textAlign = TextAlign.Center,
            )
            Text(
                text = String.format(Locale.getDefault(), "%,d원", orderItem.price),
                color = colorResource(R.color.KIWE_orange1),
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
        onClick = { _, _ -> },
    )
}
