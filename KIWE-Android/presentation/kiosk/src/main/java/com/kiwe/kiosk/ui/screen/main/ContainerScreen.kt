package com.kiwe.kiosk.ui.screen.main

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontVariation.width
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Dialog
import com.kiwe.kiosk.R
import com.kiwe.kiosk.login.LoginActivity
import com.kiwe.kiosk.main.MainSideEffect
import com.kiwe.kiosk.main.MainViewModel
import com.kiwe.kiosk.ui.component.BoldTextWithKeywords
import com.kiwe.kiosk.ui.screen.main.component.AnimatedImageSwitcher
import com.kiwe.kiosk.ui.screen.main.component.CustomPasswordInputDialog
import com.kiwe.kiosk.ui.screen.main.component.ImageButton
import com.kiwe.kiosk.ui.screen.main.component.RoundStepItem
import com.kiwe.kiosk.ui.screen.order.OrderListDialog
import com.kiwe.kiosk.ui.screen.order.ShoppingCartDialog
import com.kiwe.kiosk.ui.screen.order.ShoppingCartViewModel
import com.kiwe.kiosk.ui.theme.KIWEAndroidTheme
import com.kiwe.kiosk.ui.theme.KioskBackgroundBrush
import com.kiwe.kiosk.ui.theme.KiweGray1
import com.kiwe.kiosk.ui.theme.KiweGreen5
import com.kiwe.kiosk.ui.theme.KiweOrange1
import com.kiwe.kiosk.ui.theme.Typography
import com.kiwe.kiosk.utils.MainEnum
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import timber.log.Timber

private const val TAG = "ContainerScreen"

@Composable
fun ContainerScreen(
    viewModel: MainViewModel,
    shoppingCartViewModel: ShoppingCartViewModel,
    onBackClick: () -> Unit,
    onClickPayment: () -> Unit,
    setShoppingCartOffset: (Offset) -> Unit,
    content: @Composable () -> Unit,
) {
    val state = viewModel.collectAsState().value
    val shoppingCartState = shoppingCartViewModel.collectAsState().value
    var isShoppingCartDialogOpen by remember { mutableStateOf(false) }
    var isOrderListDialogOpen by remember { mutableStateOf(false) }
    var isQueryStateBoxOpen by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var isLogoutDialogOpen by remember { mutableStateOf(false) }

    LaunchedEffect(state.page) {
        if (state.page == 1) {
            Timber.tag(TAG).d("LaunchedEffect")
//            viewModel.speakWithTTS("음성 도움을 받으시려면, '도와줘'라고 말씀해주세요", tts)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            Timber.tag(TAG).d("onDispose")
//            tts.stop()
        }
    }

    LaunchedEffect(shoppingCartState.isVoiceOrderConfirm, shoppingCartState.shoppingCartItem) {
        isShoppingCartDialogOpen = shoppingCartState.isVoiceOrderConfirm
        delay(1000L)
        isQueryStateBoxOpen = isShoppingCartDialogOpen
        Timber.tag("ContainerScreen").d("LaunchedEffect $isQueryStateBoxOpen")
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MainSideEffect.Toast ->
                Toast
                    .makeText(
                        context,
                        sideEffect.message,
                        Toast.LENGTH_SHORT,
                    ).show()

            MainSideEffect.NavigateToLoginScreen -> {
                context.startActivity(
                    Intent(
                        context,
                        LoginActivity::class.java,
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    },
                )
            }

            MainSideEffect.NavigateToNextScreen -> TODO()
            else -> {}
        }
    }

    if (isShoppingCartDialogOpen) {
        ShoppingCartDialog(
            viewModel = shoppingCartViewModel,
            mainViewModel = viewModel,
            goOrderList = {
                isShoppingCartDialogOpen = false
                if (state.voiceShoppingCart.isNotEmpty()) {
                    // 바로 다음 화면으로 보냄
                    onClickPayment()
                } else {
                    isOrderListDialogOpen = true
                }
            },
            onClose = {
                isShoppingCartDialogOpen = false
                shoppingCartViewModel.onConfirmVoiceOrder() // 음성주문 상태 날리는 코드
            },
        )
    }

    if (isOrderListDialogOpen) {
        OrderListDialog(
            viewModel = shoppingCartViewModel,
            onClose = { isOrderListDialogOpen = false },
            onClickPayment = onClickPayment,
        )
    }

    if (state.isOrderEndTrue || state.isOrderEndFalse) {
        Timber.tag("ContainerScreenOrder").d("ordered end")
        isOrderListDialogOpen = false
        isQueryStateBoxOpen = false
        if (state.isOrderEndFalse) {
            viewModel.showSpeechScreen()
        }
    }

    QueryStateBox(
        isQueryStateBoxOpen = isQueryStateBoxOpen,
        page = state.page,
        onClose = {
            isQueryStateBoxOpen = false
        },
        onNoClick = {
            isQueryStateBoxOpen = false
            isShoppingCartDialogOpen = false
        },
        onYesClick = {
            isQueryStateBoxOpen = false
            isShoppingCartDialogOpen = false
            viewModel.showSpeechScreen()
        },
    )

    if (isLogoutDialogOpen) {
        CustomPasswordInputDialog(
            modifier = Modifier,
            onDismissRequest = { isLogoutDialogOpen = false },
            onConfirm = { password ->
                viewModel.requestSignOut(password)
                // 로그아웃 로직을 여기에 추가합니다.
                // password를 사용하여 로그아웃 확인 처리
                Timber.tag("Logout").d("비밀번호: $password")
                isLogoutDialogOpen = false
            },
        )
    }

    ContainerScreen(
        page = state.page,
        mode = state.mode,
        onBackClick = onBackClick,
        onShoppingCartDialogClick = { isShoppingCartDialogOpen = true },
        setShoppingCartOffset = setShoppingCartOffset,
        onOrderListDialogClick = { isOrderListDialogOpen = true },
        gazePoint = state.gazePoint,
        remainingTime = state.remainingTime,
        content = content,
        onLogoutRequested = { isLogoutDialogOpen = true },
    )
}

@Composable
private fun ContainerScreen(
    page: Int,
    mode: MainEnum.KioskMode,
    onBackClick: () -> Unit,
    onShoppingCartDialogClick: () -> Unit,
    setShoppingCartOffset: (Offset) -> Unit,
    onOrderListDialogClick: () -> Unit,
    gazePoint: Offset?,
    remainingTime: Long,
    content: @Composable () -> Unit,
    onLogoutRequested: () -> Unit,
) {
    gazePoint // TODO
    Scaffold(
        topBar = {
            mode // TODO
            if (page > 0) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Spacer(Modifier.weight(1f))
                        StepIndicator(modifier = Modifier.weight(6f), page)
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center,
                        ) {
                            BoldTextWithKeywords(
                                modifier = Modifier,
                                fullText = "남은 시간\n${remainingTime}초",
                                keywords = listOf("$remainingTime"),
                                brushFlag = listOf(true),
                                boldStyle = Typography.bodyMedium,
                                normalStyle = Typography.labelMedium.copy(fontSize = 10.sp),
                                alignStyle = TextAlign.Center,
                                textColor = KiweOrange1,
                            )
                        }
                    }
                    Box(modifier = Modifier, contentAlignment = Alignment.Center) {
                        AnimatedImageSwitcher(80.dp, onLogoutRequested = onLogoutRequested)
                        VoiceIntro()
                    }
                }
            }
        },
        content = {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(KioskBackgroundBrush)
                        .padding(it),
                content = {
                    content()
                },
            )
//            gazePoint?.let { gazePoint ->
//                GazeIndicator(gazePoint = gazePoint)
//            }
        },
        bottomBar = {
            if (page == 1) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp),
                ) {
                    ImageButton(
                        modifier = Modifier.weight(1F),
                        "직원호출",
                        R.drawable.ic_employee_call,
                        R.color.KIWE_gray1,
                    ) {
                        // TODO : 직원 호출
                    }
                    Spacer(Modifier.width(5.dp))
                    ImageButton(
                        modifier =
                            Modifier
                                .weight(1F)
                                .onGloballyPositioned {
                                    setShoppingCartOffset(
                                        Offset(
                                            it.positionInRoot().x,
                                            it.positionInRoot().y - it.size.height * 4,
                                        ),
                                    )
                                },
                        "장바구니",
                        R.drawable.shopping_cart,
                        R.color.KIWE_orange1,
                    ) {
                        onShoppingCartDialogClick()
                    }
                    Spacer(Modifier.width(5.dp))
                    ImageButton(
                        modifier = Modifier.weight(1F),
                        "결제하기",
                        R.drawable.card_pos,
                        R.color.KIWE_green5,
                    ) {
                        onOrderListDialogClick()
                    }
                }
            } else if (page == 2) {
                PreviousButton(
                    onBackClick = onBackClick,
                )
            } else if (page == 3) {
                PreviousButton(
                    modifier = Modifier.alpha(0.0F),
                    onBackClick = { },
                )
            }
        },
    )
}

@Composable
fun QueryStateBox(
    isQueryStateBoxOpen: Boolean,
    page: Int = 0,
    onClose: () -> Unit,
    onYesClick: () -> Unit = {},
    onNoClick: () -> Unit = {},
) {
    // 장바구니 화면에서 팝업 띄우고, 포장, 매장 화면에서 팝업 또 띄우고
    if (isQueryStateBoxOpen) {
        Timber.tag("ContainerScreen").d("QueryStateBox $page")
        Dialog(onDismissRequest = {
            onClose()
        }) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "더 주문하시겠습니까?",
                        style = Typography.titleLarge.copy(color = Color.White),
                    )
                    Text(
                        text = "음성으로 하셔도 됩니다",
                        style = Typography.titleMedium.copy(color = Color.White),
                    )
                    Row(modifier = Modifier.padding(top = 12.dp)) {
                        Box(
                            modifier =
                                Modifier
                                    .background(color = KiweGray1, shape = RoundedCornerShape(20.dp))
                                    .padding(8.dp)
                                    .clickable {
                                        onYesClick()
                                        onClose()
                                    },
                        ) {
                            Text(
                                text = "네",
                                modifier = Modifier.padding(8.dp),
                                style = Typography.titleLarge.copy(color = Color.White),
                            )
                        }

                        Box(
                            modifier =
                                Modifier
                                    .padding(start = 12.dp)
                                    .background(color = KiweGreen5, shape = RoundedCornerShape(20.dp))
                                    .padding(8.dp)
                                    .clickable {
                                        onNoClick()
                                        onClose()
                                    },
                        ) {
                            Text(
                                text = "아니오",
                                modifier = Modifier.padding(8.dp),
                                style = Typography.titleLarge.copy(color = Color.White),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GazeIndicator(gazePoint: Offset) {
    Timber.tag("GazeIndicator").d("gazePoint: $gazePoint")

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    Box(
        modifier =
            Modifier
                .offset(
                    x = gazePoint.x * screenWidth,
                    y = gazePoint.y * screenHeight,
                ).size(20.dp)
                .background(Color.Red.copy(alpha = 0.5f)),
    )
}

@Composable
fun VoiceIntro() {
    val message = "음성 도움을 받으시려면, '도와줘'라고 말씀해주세요"
    val currentMessage = rememberUpdatedState(message)
    var displayedText by remember { mutableStateOf("") }
    LaunchedEffect(currentMessage.value) {
        displayedText = ""
        currentMessage.value.forEachIndexed { index, char ->
            delay(100) // 글자가 나타나는 속도 조절 (100ms)
            displayedText += char
        }
    }
    Box(
        modifier =
            Modifier
                .offset(y = 24.dp)
                .padding(bottom = 8.dp)
                .background(Color.White.copy(alpha = 0.8f), shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        Text(text = displayedText, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun PreviousButton(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    Button(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 120.dp, vertical = 20.dp),
        onClick = onBackClick,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2e7d32),
                contentColor = Color.White,
            ),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "이전으로",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
        )
    }
}

@Composable
fun StepIndicator(
    modifier: Modifier = Modifier,
    currentStep: Int,
) {
    val steps = listOf("주문", "결제", "확인")
    if (currentStep > 0) {
        Row(
            modifier =
                modifier
                    .fillMaxWidth()
                    .padding(4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            steps.forEachIndexed { index, step ->
                if (index != 0) {
                    Spacer(Modifier.width(5.dp))
                }
                val widthWeight by animateFloatAsState(
                    targetValue = if (index == currentStep - 1) 4f else 3f,
                    label = "",
                )
                RoundStepItem(
                    title = step,
                    isActive = index == currentStep - 1,
                    isFirst = index == 0,
                    isLast = index == steps.size - 1,
                    modifier = Modifier.weight(widthWeight),
                )
            }
        }
    }
}

@Composable
fun StepItem(
    title: String,
    isActive: Boolean,
    isFirst: Boolean,
    isLast: Boolean,
    modifier: Modifier = Modifier,
) {
    isFirst and isLast // TODO
    // 색상조정 필요
    val backgroundColor = if (isActive) Color(0xFF7b4f3f) else Color(0xFFede0d4)
    val textColor = if (isActive) Color.White else Color.Gray

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Canvas(
            modifier =
                Modifier
                    .height(32.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp),
        ) {
            val path =
                Path().apply {
                    moveTo(0f, 0f) // 시작점 (왼쪽 상단)
                    lineTo(size.width - 20f, 0f) // 상단 직선
                    lineTo(size.width, size.height / 2) // 오른쪽 중간의 뾰족한 부분
                    lineTo(size.width - 20f, size.height) // 하단 직선
                    lineTo(0f, size.height) // 왼쪽 하단
                    lineTo(20f, size.height / 2) // 왼쪽 중간의 뾰족한 부분 (마지막 단계는 제외)
                    close()
                }

            drawPath(path = path, color = backgroundColor)
        }
        Text(
            text = title,
            color = textColor,
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
        )
    }
}

@Composable
@Preview
fun ContainerScreenPreview() {
    KIWEAndroidTheme {
        ContainerScreen(
            page = 1,
            mode = MainEnum.KioskMode.MANUAL,
            onBackClick = {},
            onShoppingCartDialogClick = {},
            onOrderListDialogClick = {},
            gazePoint = Offset(0f, 0f),
            content = {},
            remainingTime = 0L,
            onLogoutRequested = {},
            setShoppingCartOffset = {},
        )
    }
}

@Composable
@Preview
fun QueryStateBoxPreview() {
    KIWEAndroidTheme {
        QueryStateBox(isQueryStateBoxOpen = true, page = 0, {})
    }
}
