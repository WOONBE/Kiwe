package com.kiwe.kiosk.ui.screen.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.kiwe.kiosk.R
import com.kiwe.kiosk.main.MainViewModel
import com.kiwe.kiosk.ui.screen.main.component.AnimatedImageSwitcher
import com.kiwe.kiosk.ui.screen.main.component.ImageButton
import com.kiwe.kiosk.ui.screen.order.OrderListDialog
import com.kiwe.kiosk.ui.screen.order.ShoppingCartDialog
import com.kiwe.kiosk.ui.screen.order.ShoppingCartViewModel
import com.kiwe.kiosk.ui.theme.KIWEAndroidTheme
import com.kiwe.kiosk.ui.theme.KioskBackgroundBrush
import com.kiwe.kiosk.utils.MainEnum
import org.orbitmvi.orbit.compose.collectAsState
import timber.log.Timber

@Composable
fun ContainerScreen(
    viewModel: MainViewModel,
    shoppingCartViewModel: ShoppingCartViewModel,
    onBackClick: () -> Unit,
    onClickPayment: () -> Unit,
    content: @Composable () -> Unit,
) {
    val state = viewModel.collectAsState().value
    val shoppingCartState = shoppingCartViewModel.collectAsState().value
    var isShoppingCartDialogOpen by remember { mutableStateOf(false) }
    var isOrderListDialogOpen by remember { mutableStateOf(false) }

    LaunchedEffect(shoppingCartState.isVoiceOrderConfirm) {
        isShoppingCartDialogOpen = shoppingCartState.isVoiceOrderConfirm
    }

    if (isShoppingCartDialogOpen) {
        ShoppingCartDialog(
            viewModel = shoppingCartViewModel,
            goOrderList = {
                isShoppingCartDialogOpen = false
                isOrderListDialogOpen = true
            },
            onClose = {
                isShoppingCartDialogOpen = false
                shoppingCartViewModel.onConfirmVoiceOrder()
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
    ContainerScreen(
        page = state.page,
        mode = state.mode,
        onBackClick = onBackClick,
        onShoppingCartDialogClick = { isShoppingCartDialogOpen = true },
        onOrderListDialogClick = { isOrderListDialogOpen = true },
        gazePoint = state.gazePoint,
        content = content,
    )
}

@Composable
private fun ContainerScreen(
    page: Int,
    mode: MainEnum.KioskMode,
    onBackClick: () -> Unit,
    onShoppingCartDialogClick: () -> Unit,
    onOrderListDialogClick: () -> Unit,
    gazePoint: Offset?,
    content: @Composable () -> Unit,
) {
    gazePoint // TODO
    Scaffold(
        topBar = {
            mode // TODO
            if (page > 0) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    StepIndicator(page)
                    AnimatedImageSwitcher(100.dp)
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
                        "이전으로",
                        R.drawable.arrow_square_left,
                        R.color.KIWE_gray1,
                    ) {
                        onBackClick()
                    }
                    Spacer(Modifier.width(5.dp))
                    ImageButton(
                        modifier = Modifier.weight(1F),
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
            } else if (page > 0) {
                PreviousButton(onBackClick = onBackClick)
            }
        },
    )
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
fun PreviousButton(onBackClick: () -> Unit) {
    Button(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 120.dp, vertical = 20.dp),
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
fun StepIndicator(currentStep: Int) {
    val steps = listOf("주문", "결제", "확인")
    if (currentStep > 0) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .padding(top = 4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            steps.forEachIndexed { index, step ->
                StepItem(
                    title = step,
                    isActive = index == currentStep - 1,
                    isFirst = index == 0,
                    isLast = index == steps.size - 1,
                    modifier = Modifier.weight(1f),
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
            page = 0,
            mode = MainEnum.KioskMode.MANUAL,
            onBackClick = {},
            onShoppingCartDialogClick = {},
            onOrderListDialogClick = {},
            gazePoint = Offset(0f, 0f),
            content = {},
        )
    }
}
