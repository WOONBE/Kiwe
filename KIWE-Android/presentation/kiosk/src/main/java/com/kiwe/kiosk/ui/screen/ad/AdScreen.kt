package com.kiwe.kiosk.ui.screen.ad

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.kiwe.kiosk.R
import com.kiwe.kiosk.ui.theme.KIWEAndroidTheme
import kotlinx.coroutines.delay

@Composable
fun AdScreen(resetMainViewModel: () -> Unit = {}) {
    val imageResIds =
        listOf(
            R.drawable.img_ad_a,
            R.drawable.img_ad_b,
            R.drawable.img_ad_c,
            R.drawable.img_ad_d,
            R.drawable.img_ad_e,
        )

    var currentPage by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        resetMainViewModel()
    }

    LaunchedEffect(key1 = currentPage) {
        delay(5000L) // 5초 대기
        currentPage = (currentPage + 1) % imageResIds.size // 다음 이미지로 이동
    }
    // 이미지 전환 시 Crossfade 애니메이션 적용
    Crossfade(targetState = currentPage, label = "") { page ->
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = imageResIds[page]),
                contentDescription = null,
                contentScale = ContentScale.Crop, // 이미지가 화면에 꽉 차게 표시되도록 설정
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Preview
@Composable
fun AdScreenPreview() {
    KIWEAndroidTheme {
        AdScreen()
    }
}
