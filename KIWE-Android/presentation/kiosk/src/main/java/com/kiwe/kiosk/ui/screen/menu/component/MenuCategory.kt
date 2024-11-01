package com.kiwe.kiosk.ui.screen.menu.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.kiwe.kiosk.ui.theme.KIWEAndroidTheme

@Composable
fun MenuCategory(
    categoryImage: String,
    categoryName: String,
) {
    Box(
        modifier =
            Modifier
                .width(360.dp)
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp)),
    ) {
        Column(
            modifier = Modifier.background(Color.White).padding(16.dp),
        ) {
            Box(
                modifier =
                    Modifier
                        .aspectRatio(1f)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(20.dp)),
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter =
                        rememberAsyncImagePainter(
                            model = categoryImage,
                            contentScale = ContentScale.Crop,
                        ),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
            }
            Text(
                modifier = Modifier.fillMaxWidth().padding(top = 14.dp),
                text = categoryName,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 84.sp),
            )
        }
    }
}

@Preview
@Composable
fun MenuCategoryPreview() {
    KIWEAndroidTheme {
        MenuCategory(categoryImage = "suspendisse", categoryName = "카테고리")
    }
}