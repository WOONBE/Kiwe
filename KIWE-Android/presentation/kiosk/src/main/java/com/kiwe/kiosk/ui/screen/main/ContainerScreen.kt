package com.kiwe.kiosk.ui.screen.main

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.kiwe.domain.model.MenuCategoryParam
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
import com.kiwe.kiosk.ui.screen.order.ShoppingCartState
import com.kiwe.kiosk.ui.screen.order.ShoppingCartViewModel
import com.kiwe.kiosk.ui.screen.utils.prefixingImagePaths
import com.kiwe.kiosk.ui.theme.KIWEAndroidTheme
import com.kiwe.kiosk.ui.theme.KioskBackgroundBrush
import com.kiwe.kiosk.ui.theme.KiweGray1
import com.kiwe.kiosk.ui.theme.KiweGreen5
import com.kiwe.kiosk.ui.theme.KiweOrange1
import com.kiwe.kiosk.ui.theme.KiweSilver1
import com.kiwe.kiosk.ui.theme.KiweWhite1
import com.kiwe.kiosk.ui.theme.KiweYellow
import com.kiwe.kiosk.ui.theme.Typography
import com.kiwe.kiosk.utils.MainEnum
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import timber.log.Timber
import java.util.Locale

private const val TAG = "MySpeech"

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
    var isMySpeechInputTextOpen by remember { mutableStateOf(false) }
    LaunchedEffect(state.page) {
        if (state.page == 0) {
            isShoppingCartDialogOpen = false
        }
    }

    LaunchedEffect(state.mySpeechText, state.isScreenShowing) {
        if (isQueryStateBoxOpen || state.isScreenShowing) {
            // true면 보이도록
            Timber.tag(TAG).d("LaunchedEffect $isQueryStateBoxOpen") // yes no dialog는 안열렸음
            isMySpeechInputTextOpen = state.mySpeechText.isNotEmpty()
        }
    }

    if (isMySpeechInputTextOpen) {
        Timber.tag(TAG).d("MySpeechInputText $isMySpeechInputTextOpen //  ${state.mySpeechText}")
        // 텍스트가 일단 뽑힘
        MySpeechInputText(
            isMySpeechInputTextOpen = isMySpeechInputTextOpen,
            sentence = state.mySpeechText,
            onAnimationEnd = {
                isMySpeechInputTextOpen = false // 애니메이션 끝난 후 자동으로 숨기기
            },
        )
    }

    LaunchedEffect(shoppingCartState.isVoiceOrderConfirm) {
         // , shoppingCartState.shoppingCartItem
        if (shoppingCartState.isVoiceOrderConfirm) {
            isShoppingCartDialogOpen = true
            delay(2000L)
            isQueryStateBoxOpen = isShoppingCartDialogOpen
            Timber.tag("ContainerScreen").d("LaunchedEffect $isQueryStateBoxOpen")
        }
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MainSideEffect.Toast -> {
//                Toast
//                    .makeText(
//                        context,
//                        sideEffect.message,
//                        Toast.LENGTH_SHORT,
//                    ).show()
            }

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
        viewModel.openShoppingCart()
        Timber
            .tag("추천")
            .d(
                "${state.voiceShoppingCart}\n " +
                    "${state.isOrderEndTrue} \n " +
                    "${state.isOrderEndFalse}",
            )
        ShoppingCartDialog(
            viewModel = shoppingCartViewModel,
            mainViewModel = viewModel,
            onClickPayment = {
                viewModel.closeShoppingCart()
                isShoppingCartDialogOpen = false
                if (state.voiceShoppingCart.isNotEmpty()) {
                    // 바로 다음 화면으로 보냄
                    onClickPayment()
                } else {
                    onClickPayment()
//                    isOrderListDialogOpen = true
                }
            },
            onClose = {
                viewModel.closeShoppingCart()
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

    if (state.isOrderEndTrue || state.isOrderEndFalse) { // 계속 안해?
        Timber.tag("추천").d("ordered end ${state.isOrderEndTrue}")
        // 여기까지는 오는데 뭐가 문제일까
        isOrderListDialogOpen = false
        isQueryStateBoxOpen = false
        if (state.isOrderEndFalse) {
            viewModel.showSpeechScreen()
        } else {
            viewModel.closeSpeechScreen()
        }
        viewModel.clearOrderEndState()
    }

    QueryStateBox(
        isQueryStateBoxOpen = isQueryStateBoxOpen,
        page = state.page,
        onClose = {
            isQueryStateBoxOpen = false
        },
        onNoClick = {
            // 더 이상 주문하지 않겠다 -> 결제도와달라는 의미
            isQueryStateBoxOpen = false
            isShoppingCartDialogOpen = false
        },
        onYesClick = {
            isQueryStateBoxOpen = false
            isShoppingCartDialogOpen = false
            viewModel.showSpeechScreen()
        },
        isMySpeechInputTextOpen = isMySpeechInputTextOpen,
        sentence = state.mySpeechText,
    )

    if (state.isAddCartTrue || state.isAddCartFalse) {
        Timber.tag("ContainerScreenOrder").d("rc end")
        if (state.isAddCartTrue) {
            // 장바구니에 넣어줌
            shoppingCartViewModel.onVoiceResult(state.voiceResult)
            viewModel.clearVoiceRecord()
            // 장바구니 오픈
            viewModel.openShoppingCart()
        }

        if (state.isAddCartFalse) {
            // 추천한 거 안담으면 그냥 장바구니 오픈
            shoppingCartViewModel.onVoiceResult(state.voiceResult) // 담겨있던거 써야되는데,
            viewModel.clearVoiceRecord()
            // 장바구니 오픈
            viewModel.openShoppingCart()
        }
        viewModel.clearRecommendHistory()
    }

    if (state.isRecommend.isNotEmpty()) {
        RecommendStateBox(
            recommendString = state.isRecommend,
            recommendMenu = state.recommendMenu,
            subRecommendMenu = state.subRecommendMenu,
            onClose = { viewModel.clearRecommendHistory() },
            onYesClick = {
                viewModel.clearRecommendHistory()
            },
            isMySpeechInputTextOpen = isMySpeechInputTextOpen,
            sentence = state.mySpeechText,
        )
    }

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
        shoppingCartState = shoppingCartState,
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
    shoppingCartState: ShoppingCartState,
    onBackClick: () -> Unit,
    onShoppingCartDialogClick: () -> Unit,
    setShoppingCartOffset: (Offset) -> Unit,
    onOrderListDialogClick: () -> Unit,
    gazePoint: Offset?,
    remainingTime: Long,
    content: @Composable () -> Unit,
    onLogoutRequested: () -> Unit,
) {
    val rotation = remember { Animatable(0f) }
    val borderColor = remember { Animatable(Color.Transparent) }
    val orderCount = "총 ${shoppingCartState.shoppingCartItem.size}건"
    // orderCount가 변경될 때 애니메이션 실행
    LaunchedEffect(orderCount) {
        delay(1300L)
        rotation.animateTo(
            targetValue = 5f,
            animationSpec = tween(durationMillis = 100, easing = LinearEasing),
        )
        rotation.animateTo(
            targetValue = -5f,
            animationSpec = tween(durationMillis = 100, easing = LinearEasing),
        )
        rotation.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 100, easing = LinearEasing),
        )
    }
    LaunchedEffect(orderCount) {
        delay(1300L)
        // 테두리 색깔 애니메이션
        borderColor.animateTo(
            targetValue = KiweYellow,
            animationSpec = tween(durationMillis = 300),
        )
        borderColor.animateTo(
            targetValue = Color.Transparent,
            animationSpec = tween(durationMillis = 300),
        )
    }
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
                        "",
                        "직원호출",
                        R.drawable.ic_employee_call,
                        R.color.KIWE_gray1,
                    ) {
                        // TODO : 직원 호출
                    }
                    Spacer(Modifier.width(5.dp))
                    Box(
                        modifier =
                            Modifier
                                .weight(1F)
                                .graphicsLayer(rotationZ = rotation.value) // 흔들림 애니메이션 추가
                                .onGloballyPositioned {
                                    setShoppingCartOffset(
                                        Offset(
                                            it.positionInRoot().x,
                                            it.positionInRoot().y - it.size.height * 4,
                                        ),
                                    )
                                },
                    ) {
                        ImageButton(
                            modifier = Modifier,
                            orderCount,
                            "장바구니",
                            R.drawable.shopping_cart,
                            R.color.KIWE_brown4,
                            borderColor.value,
                        ) {
                            onShoppingCartDialogClick()
                        }
                    }
                    Spacer(Modifier.width(5.dp))
                    var cost = 0
                    for (shoppingCartItem in shoppingCartState.shoppingCartItem) {
                        cost += shoppingCartItem.count * shoppingCartItem.totalPrice
                    }
                    val totalPrice = String.format(Locale.getDefault(), "%,d원", cost)
                    ImageButton(
                        modifier = Modifier.weight(1F),
                        totalPrice,
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
fun RecommendStateBox(
    recommendString: String,
    recommendMenu: MenuCategoryParam,
    isMySpeechInputTextOpen: Boolean,
    sentence: String,
    subRecommendMenu: List<MenuCategoryParam>,
    subRecommendString: String = "",
    onClose: () -> Unit,
    onYesClick: () -> Unit = {}, // 바로 장바구니에 넣기
) {
    subRecommendString
    onYesClick
    if (recommendString.isNotEmpty()) {
        Dialog(onDismissRequest = {
            onClose()
        }) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .clickable {
                            onClose()
                        },
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "추천 메뉴",
                        style = Typography.titleLarge.copy(color = KiweOrange1),
                    )
                    Box(
                        modifier =
                            Modifier
                                .padding(vertical = 12.dp)
                                .size(120.dp)
                                .clip(shape = RoundedCornerShape(10.dp))
                                .border(2.dp, KiweOrange1, RoundedCornerShape(10.dp)),
                    ) {
                        val imgUrl = recommendMenu.imgPath.prefixingImagePaths()
                        Timber.tag("추천").d(imgUrl)
                        Image(
                            modifier = Modifier.background(KiweWhite1).padding(5.dp),
                            painter =
                                rememberAsyncImagePainter(
                                    model = imgUrl,
                                ),
                            contentScale = ContentScale.Crop,
                            contentDescription = recommendString,
                        )
                    }
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = recommendMenu.name,
                        style = Typography.titleMedium.copy(color = KiweWhite1),
                    )
                    Text(
                        modifier = Modifier.padding(vertical = 5.dp, horizontal = 20.dp),
                        text = recommendMenu.description,
                        style = Typography.bodyMedium.copy(fontSize = 12.sp, color = KiweSilver1),
                    )
                    Text(
                        modifier = Modifier.padding(vertical = 5.dp, horizontal = 20.dp),
                        text = String.format(Locale.KOREA, "%,d원", recommendMenu.price),
                        style = Typography.bodyMedium.copy(fontSize = 20.sp, color = KiweWhite1),
                    )
                    Spacer(Modifier.height(10.dp))
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "그 외의 추천 메뉴",
                        style = Typography.bodyMedium.copy(color = KiweSilver1),
                    )
                    Row(
                        modifier =
                            Modifier
                                .padding(top = 12.dp)
                                .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        // 첫 번째 추천 메뉴 박스
                        Timber.tag("추천 첫번째").d(subRecommendMenu.toString())
                        if (subRecommendMenu.getOrNull(0) != null) {
                            Box(
                                modifier =
                                    Modifier
                                        .size(80.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(KiweWhite1),
                                contentAlignment = Alignment.Center,
                            ) {
                                Column(
                                    modifier =
                                        Modifier
                                            .fillMaxSize()
                                            .padding(5.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                ) {
                                    val imgPath1 = subRecommendMenu[0].imgPath.prefixingImagePaths()
                                    Image(
                                        painter =
                                            rememberAsyncImagePainter(
                                                model = imgPath1, // 첫 번째 이미지 URL
                                            ),
                                        contentDescription = "추천 메뉴 1",
                                        contentScale = ContentScale.Crop,
                                        modifier =
                                            Modifier
                                                .size(50.dp)
                                                .clip(RoundedCornerShape(10.dp)),
                                    )
                                    Text(
                                        modifier = Modifier,
                                        text = subRecommendMenu[0].name, // 첫 번째 메뉴 이름
                                        style =
                                            Typography.bodySmall.copy(
                                                fontSize = 8.sp,
                                                color = KiweGray1,
                                            ),
                                        textAlign = TextAlign.Center,
                                    )
                                }
                            }
                        }

                        // 두 번째 추천 메뉴 박스
                        if (subRecommendMenu.getOrNull(1) != null) {
                            Box(
                                modifier =
                                    Modifier
                                        .size(80.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(KiweWhite1),
                                contentAlignment = Alignment.Center,
                            ) {
                                Column(
                                    modifier =
                                        Modifier
                                            .fillMaxSize()
                                            .padding(5.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                ) {
                                    val imgPath2 = subRecommendMenu[1].imgPath.prefixingImagePaths()
                                    Image(
                                        painter =
                                            rememberAsyncImagePainter(
                                                model = imgPath2, // 두 번째 이미지 URL
                                            ),
                                        contentDescription = "추천 메뉴 2",
                                        contentScale = ContentScale.Crop,
                                        modifier =
                                            Modifier
                                                .size(50.dp)
                                                .clip(RoundedCornerShape(10.dp)),
                                    )
                                    Text(
                                        modifier = Modifier,
                                        text = subRecommendMenu[1].name, // 두 번째 메뉴 이름
                                        style =
                                            Typography.bodySmall.copy(
                                                fontSize = 8.sp,
                                                color = KiweGray1,
                                            ),
                                        textAlign = TextAlign.Center,
                                    )
                                }
                            }
                        }
                    }

                    MySpeechInputText(
                        isMySpeechInputTextOpen = isMySpeechInputTextOpen,
                        sentence = sentence,
                    )
                }
            }
        }
    }
}

@Composable
fun QueryStateBox(
    isQueryStateBoxOpen: Boolean,
    isMySpeechInputTextOpen: Boolean,
    sentence: String,
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
                        text = "추가로 주문하시겠습니까?",
                        style = Typography.titleMedium.copy(color = KiweWhite1, fontSize = 20.sp),
                    )
                    Text(
                        text = "음성으로 하셔도 됩니다",
                        style = Typography.bodyMedium.copy(color = KiweWhite1, fontSize = 16.sp),
                    )
                    Spacer(Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.padding(top = 10.dp).padding(horizontal = 40.dp),
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier =
                                Modifier
                                    .weight(1F)
                                    .background(color = KiweGray1, shape = RoundedCornerShape(10.dp))
                                    .padding(10.dp)
                                    .clickable {
                                        onYesClick()
                                        onClose()
                                    },
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "네",
                                modifier = Modifier.padding(8.dp),
                                style =
                                    Typography.titleLarge.copy(
                                        fontSize = 20.sp,
                                        color = KiweWhite1,
                                    ),
                            )
                        }

                        Box(
                            modifier =
                                Modifier
                                    .weight(1F)
                                    .background(color = KiweGreen5, shape = RoundedCornerShape(10.dp))
                                    .padding(10.dp)
                                    .clickable {
                                        onNoClick()
                                        onClose()
                                    },
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "아니오",
                                modifier = Modifier.padding(8.dp),
                                style =
                                    Typography.titleLarge.copy(
                                        fontSize = 20.sp,
                                        color = KiweWhite1,
                                    ),
                            )
                        }
                    }
                    MySpeechInputText(
                        isMySpeechInputTextOpen = isMySpeechInputTextOpen,
                        sentence = sentence,
                    )
                }
            }
        }
    }
}

@Composable
fun MySpeechInputText(
    isMySpeechInputTextOpen: Boolean,
    sentence: String,
    onAnimationEnd: () -> Unit = {},
) {
    if (isMySpeechInputTextOpen) { // 이게 true일거라서 보여야됨
        Timber.tag(TAG).d("Composable $isMySpeechInputTextOpen")
        var displayedText by remember { mutableStateOf("") }
        val scope = rememberCoroutineScope()
        LaunchedEffect(sentence) {
            displayedText = ""
            scope.launch {
                for (char in sentence) {
                    displayedText += char
                    delay(100)
                    Timber.tag(TAG).d("MySpeechInputText $displayedText")
                }
                onAnimationEnd() // 글자가 완성되면 할 것
                // 글자 완성되면 플래그 하나 더 해서 그걸로 다뤄야할 것 같다.
            }
        }

        Text(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .heightIn(min = 48.dp)
                    .padding(top = 12.dp),
            text = "\"" + displayedText + "\"",
            textAlign = TextAlign.Center,
            style = Typography.titleLarge.copy(color = Color.White),
        )
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
                .offset(y = 30.dp)
                .padding(bottom = 8.dp)
                .background(Color.White.copy(alpha = 0.8f), shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        Text(text = displayedText, style = Typography.bodyMedium.copy(fontSize = 12.sp))
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
                .padding(horizontal = 120.dp, vertical = 5.dp),
        shape = RoundedCornerShape(10.dp),
        onClick = onBackClick,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = KiweGreen5,
                contentColor = KiweWhite1,
            ),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "이전으로",
            textAlign = TextAlign.Center,
            style = Typography.titleLarge.copy(fontSize = 20.sp),
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
            shoppingCartState = ShoppingCartState(),
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
        QueryStateBox(
            isQueryStateBoxOpen = true,
            page = 0,
            onClose = {},
            onYesClick = {},
            isMySpeechInputTextOpen = true,
            sentence = "나는 무슨말을 할까",
        )
    }
}

@Composable
@Preview
fun RecommendStateBoxPreview() {
    KIWEAndroidTheme {
        RecommendStateBox(
            recommendString = "추천 문구",
            recommendMenu =
                MenuCategoryParam(
                    id = 3913,
                    category = "taciti",
                    categoryNumber = 3413,
                    hotOrIce = "sumo",
                    name = "디카페인 아메리카노",
                    price = 3282,
                    description = "fabulas",
                    imgPath = "lacinia",
                ),
            subRecommendMenu =
                listOf(
                    MenuCategoryParam(
                        id = 2986,
                        category = "curabitur",
                        categoryNumber = 9225,
                        hotOrIce = "pulvinar",
                        name = "Clara Herman",
                        price = 9414,
                        description = "conclusionemque",
                        imgPath = "definiebas",
                    ),
                    MenuCategoryParam(
                        id = 4061,
                        category = "pericula",
                        categoryNumber = 8980,
                        hotOrIce = "vivamus",
                        name = "Jerry Hayden",
                        price = 8638,
                        description = "eruditi",
                        imgPath = "docendi",
                    ),
                ),
            onClose = {},
            onYesClick = {},
            isMySpeechInputTextOpen = true,
            sentence = "나는 무슨말을 할까",
        )
    }
}

@Composable
@Preview
fun SpeechMyTextInput() {
    KIWEAndroidTheme {
        MySpeechInputText(true, sentence = "나는 무슨말을 할까", onAnimationEnd = {})
    }
}
