package com.kiwe.kiosk.ui.screen.payment

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiwe.kiosk.ui.screen.order.ShoppingCartViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun PaymentScreen(
    modifier: Modifier = Modifier,
    viewModel: PaymentViewModel = hiltViewModel(),
    shoppingCartViewModel: ShoppingCartViewModel = hiltViewModel(),
    onCompletePayment: () -> Unit = {},
    onEnterScreen: (Int) -> Unit = {},
) {
    val paymentState = viewModel.collectAsState().value
    val shoppingCartState = shoppingCartViewModel.collectAsState().value

    val pagerState =
        rememberPagerState(pageCount = {
            PaymentStatus.entries.size
        })
    LaunchedEffect(Unit) {
        onEnterScreen(2)
    }
    LaunchedEffect(paymentState.completePayment) {
        if (paymentState.completePayment) {
            onCompletePayment()
            shoppingCartViewModel.onClearAllItem()
        }
    }
    HorizontalPager(
        modifier = modifier,
        state = pagerState,
        userScrollEnabled = true, // FIXME : 테스트 후 false
    ) { page ->
        when (PaymentStatus.entries[page]) {
            PaymentStatus.TAKEOUT -> {
                TakeOutChoiceScreen(
                    modifier = Modifier.fillMaxSize(),
                    onPackagingClick = {
                        viewModel.postOrder(paymentState.kioskId, shoppingCartState)
                        viewModel.showDialog()
                    },
                    onStoreClick = {
                        viewModel.postOrder(paymentState.kioskId, shoppingCartState)
                        viewModel.showDialog()
                    },
                )
                if (paymentState.showDialog) {
                    CardCreditDialog(
                        onDismissRequest = {
                            viewModel.hideDialog()
                            viewModel.cancelPayment()
                        },
                        remainingTime = paymentState.remainingTime,
                        totalAmount = shoppingCartState.shoppingCartItem.sumOf { it.totalPrice * it.count },
                        cardNumber = paymentState.userCardNumber,
                    )
                }
            }
        }
    }
}

enum class PaymentStatus {
    TAKEOUT,
}
