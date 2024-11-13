package com.kiwe.kiosk.ui.screen.main.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kiwe.kiosk.R
import com.kiwe.kiosk.utils.noRippleClickable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun AnimatedImageSwitcher(
    size: Dp = 200.dp,
    onLogoutRequested: () -> Unit,
) {
    // 현재 보여지는 이미지를 제어하기 위한 상태
    var imageVisible by remember { mutableStateOf(true) }
    var clickCount by remember { mutableIntStateOf(0) }
    var lastClickTime by remember { mutableLongStateOf(0L) }
    val scope = rememberCoroutineScope()

    // 일정한 간격으로 이미지를 전환하는 효과
    LaunchedEffect(Unit) {
        while (true) {
            // 100~500ms 사이의 랜덤 전환 간격
            val switchInterval = Random.nextLong(100, 500)
            delay(switchInterval)
            imageVisible = !imageVisible
        }
    }

    // 이미지 레이아웃
    Box(
        modifier =
            Modifier
                .size(size)
                .noRippleClickable {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastClickTime > 5000) {
                        // 5초가 지났다면 클릭 카운트 초기화
                        clickCount = 1
                    } else {
                        clickCount++
                    }
                    lastClickTime = currentTime

                    if (clickCount >= 7) {
                        scope.launch {
                            onLogoutRequested()
                        }
                        clickCount = 0 // 카운트 초기화
                    }
                },
        // 원하는 크기로 조절 가능
    ) {
        if (imageVisible) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_playstore_nobg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_playstore_nobg_open),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
