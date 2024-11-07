package com.kiwe.kiosk.ui.screen.payment

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiwe.kiosk.ui.screen.order.ShoppingCartViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun PaymentScreen(
    modifier: Modifier = Modifier,
    viewModel: PaymentViewModel = hiltViewModel(),
    shoppingCartViewModel: ShoppingCartViewModel = hiltViewModel(),
    onEnterScreen: (Int) -> Unit,
) {
    val shoppingCartState = shoppingCartViewModel.collectAsState().value
    var showDialog by remember { mutableStateOf(false) }

    val pagerState =
        rememberPagerState(pageCount = {
            PaymentStatus.entries.size
        })
    LaunchedEffect(Unit) {
        onEnterScreen(3)
    }
    HorizontalPager(
        modifier = modifier,
        state = pagerState,
        userScrollEnabled = true, // FIXME : 테스트 후 false
    ) { page ->
        when (PaymentStatus.entries[page]) {
            PaymentStatus.TAKEOUT -> {
                TakeOutChoiceScreen(
                    modifier = Modifier,
                    onPackagingClick = {
                        viewModel.postOrder(shoppingCartState)
                    },
                    onStoreClick = {
                        viewModel.postOrder(shoppingCartState)
                        viewModel.navigateToPaymentStatus(pagerState, PaymentStatus.PAYMENT_METHOD)
                    },
                )

            }

            PaymentStatus.PAYMENT_METHOD -> {
                PaymentChoiceScreen(
                    modifier = Modifier,
                    onQrClick = {

                    },
                    onCardClick = {
                        showDialog = true
                    },
                )
                if (showDialog) {
                    viewModel.startConfirmPayment(kioskId = 1)
                    CardCreditDialog(
                        onDismissRequest = {
                            showDialog = false
                            viewModel.cancelPayment()
                        },
                        totalAmount = shoppingCartState.shoppingCartItem.sumOf { it.totalPrice * it.count },
                    )
                }
            }

            PaymentStatus.CARD -> {
                TakeOutChoiceScreen(
                    modifier = Modifier,
                )
            }
        }
    }
}

enum class PaymentStatus {
    TAKEOUT,
    PAYMENT_METHOD,
    CARD,
}
