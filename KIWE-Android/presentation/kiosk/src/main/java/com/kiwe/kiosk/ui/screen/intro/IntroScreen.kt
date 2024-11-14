package com.kiwe.kiosk.ui.screen.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.kiwe.kiosk.R
import com.kiwe.kiosk.main.MainSideEffect
import com.kiwe.kiosk.main.MainViewModel
import com.kiwe.kiosk.ui.theme.KIWEAndroidTheme
import com.kiwe.kiosk.ui.theme.Typography
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import timber.log.Timber

@Composable
fun IntroScreen(
    viewModel: MainViewModel,
    onEnterScreen: (Int) -> Unit,
    onComfortClick: () -> Unit,
    onHelpClick: () -> Unit,
) {
    val state = viewModel.collectAsState().value
    viewModel.collectSideEffect {
        when (it) {
            MainSideEffect.NavigateToNextScreen -> TODO()
            is MainSideEffect.Toast -> TODO()
            MainSideEffect.NavigateToLoginScreen -> TODO()
        }
    }

    LaunchedEffect(Unit) {
        onEnterScreen(0)
    }
    IntroScreen(
        onComfortClick = onComfortClick,
        onHelpClick = onHelpClick,
        faceDetection = state.isExistPerson,
    )
}

@Composable
private fun IntroScreen(
    onComfortClick: () -> Unit,
    onHelpClick: () -> Unit,
    faceDetection: Boolean,
) {
    onComfortClick
    Box(
        modifier =
            Modifier
                .padding(horizontal = 20.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier =
                    Modifier.size(200.dp).clip(CircleShape).padding(bottom = 20.dp),
                painter =
                    rememberAsyncImagePainter(
                        model = R.drawable.ic_launcher_playstore_nobg,
                        contentScale = ContentScale.Crop,
                    ),
                contentDescription = "logo",
            )
            Text(
                text = "안녕하세요",
                textAlign = TextAlign.Center,
                style = Typography.titleLarge.copy(fontSize = 44.sp),
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = "키오스크 사용이 익숙하신가요?",
                textAlign = TextAlign.Center,
                style = Typography.titleLarge,
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier =
                        Modifier
                            .weight(1f)
                            .background(color = Color(0xFF6d6d6d), shape = RoundedCornerShape(30.dp))
                            .heightIn(min = 180.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(text = "😊", textAlign = TextAlign.Center, fontSize = 64.sp)
                    Text(
                        text = "익숙해요",
                        style = Typography.titleLarge.copy(color = Color.White),
                    )
                }
                Timber.tag("detection").d("$faceDetection")

                Column(
                    modifier =
                        Modifier
                            .weight(1f)
                            .background(color = Color(0xFF2e7d32), shape = RoundedCornerShape(30.dp))
                            .heightIn(min = 180.dp)
                            .clickable {
                                onHelpClick()
                            },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(text = "🤷‍♂️", textAlign = TextAlign.Center, fontSize = 64.sp)
                    Text(
                        text = "도움이\n필요해요",
                        textAlign = TextAlign.Center,
                        style = Typography.titleLarge.copy(color = Color.White),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun IntroScreenPreview() {
    KIWEAndroidTheme {
        Surface {
            IntroScreen({}, {}, false)
        }
    }
}
