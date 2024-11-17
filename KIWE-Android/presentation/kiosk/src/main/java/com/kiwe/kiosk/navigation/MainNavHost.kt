package com.kiwe.kiosk.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kiwe.kiosk.main.MainSideEffect
import com.kiwe.kiosk.main.MainViewModel
import com.kiwe.kiosk.ui.screen.ad.AdScreen
import com.kiwe.kiosk.ui.screen.intro.IntroScreen
import com.kiwe.kiosk.ui.screen.main.ContainerScreen
import com.kiwe.kiosk.ui.screen.menu.MenuScreen
import com.kiwe.kiosk.ui.screen.order.OrderScreen
import com.kiwe.kiosk.ui.screen.order.ShoppingCartViewModel
import com.kiwe.kiosk.ui.screen.payment.PaymentScreen
import com.kiwe.kiosk.ui.screen.receipt.ReceiptScreen
import com.kiwe.kiosk.ui.screen.speech.SpeechScreen
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import timber.log.Timber

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainNavHost() {
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = hiltViewModel()
    val shoppingCartViewModel: ShoppingCartViewModel = hiltViewModel()
    val state = mainViewModel.collectAsState().value
    var shoppingCartOffset by remember { mutableStateOf(Offset.Zero) }

    // 사이드 이펙트 처리
    mainViewModel.collectSideEffect {
        when (it) {
            is MainSideEffect.Toast -> {}
            MainSideEffect.NavigateToNextScreen -> {}
            MainSideEffect.NavigateToAdvertisement -> {}
            MainSideEffect.ClearCart -> {
                shoppingCartViewModel.onClearAllItem() // 장바구니 아이템 삭제
            }

            MainSideEffect.NavigateToLoginScreen -> {}
        }
    }

    Surface {
        Scaffold(
            content = {
                ContainerScreen(
                    viewModel = mainViewModel,
                    shoppingCartViewModel = shoppingCartViewModel,
                    onBackClick = { navController.navigateUp() },
                    onClickPayment = {
                        navController.navigate(MainRoute.PAYMENT.route)
                    },
                    setShoppingCartOffset = { offset -> shoppingCartOffset = offset },
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = MainRoute.AD.route,
                        exitTransition = { ExitTransition.None },
                        enterTransition = { EnterTransition.None },
                    ) {
                        composable(route = MainRoute.AD.route) {
                            AdScreen {
                                // 광고화면으로 오면 mainViewModel 청소
                                mainViewModel.resetMainViewModel()
                            }
                        }
                        composable(route = MainRoute.INTRO.route) {
                            IntroScreen(
                                viewModel = mainViewModel,
                                onEnterScreen = { page ->
                                    mainViewModel.setPage(page)
                                },
                                onComfortClick = {},
                                onHelpClick = {
                                    navController.navigate(MainRoute.ORDER.route)
                                    mainViewModel.startSpeechRecognition() // 음성인식 ON
                                },
                            )
                        }
                        composable(route = MainRoute.ORDER.route) {
                            OrderScreen(
                                shoppingCartViewModel = shoppingCartViewModel,
                                getShoppingCartPosition = { shoppingCartOffset },
                            ) { page ->
                                mainViewModel.startSpeechRecognition() // 음성인식 ON
                                mainViewModel.setPage(page)
                            }
                        }
                        composable(route = MainRoute.MENU.route) {
                            MenuScreen(
                                viewModel = mainViewModel,
                                onCategoryClick = { category, page ->
                                    navController.navigate(MainRoute.ORDER.route)
                                    mainViewModel.setPage(page)
                                },
                            )
                        }
                        composable(route = MainRoute.PAYMENT.route) {
                            PaymentScreen(
                                shoppingCartViewModel = shoppingCartViewModel,
                                mainViewModel = mainViewModel,
                                onCompletePayment = { orderNumber ->
                                    navController.navigate("${MainRoute.RECEIPT.route}/$orderNumber")
                                },
                            ) { page ->
                                mainViewModel.setPage(page)
                            }
                        }
                        composable(
                            route = "${MainRoute.RECEIPT.route}/{orderNumber}",
                            arguments =
                                listOf(
                                    navArgument("orderNumber") {
                                        defaultValue = "1001"
                                    },
                                ),
                        ) { backStackEntry ->
                            val orderNumber =
                                backStackEntry.arguments?.getString("orderNumber") ?: "1001"
                            Timber.tag("그바르디올").d("${javaClass.simpleName} : $orderNumber")
                            ReceiptScreen(
                                orderNumber = orderNumber,
                                onEnterScreen = { page ->
                                    mainViewModel.setPage(page)
                                },
                                onBackHome = {
                                    mainViewModel.stopSpeechRecognition()
                                    navController.navigate(MainRoute.AD.route) {
                                        popUpTo(MainRoute.AD.route)
                                        mainViewModel.setPage(0)
                                    }
                                },
                            )
                        }
                    }
                }
            },
        )

        LaunchedEffect(state.isExistPerson, state.page) {
            if (state.isExistPerson && state.page == 0) {
                navController.navigate(MainRoute.ORDER.route) {
                    popUpTo(MainRoute.ORDER.route)
                }
            }

            if (!state.isExistPerson) {
                navController.navigate(MainRoute.AD.route) {
                    popUpTo(MainRoute.AD.route)
                    mainViewModel.setPage(0)
                }
            }
        }

        if (state.page > 0) {
            SpeechScreen(
                mainViewModel = mainViewModel,
                shoppingCartViewModel = shoppingCartViewModel,
            )
        }
    }
}
