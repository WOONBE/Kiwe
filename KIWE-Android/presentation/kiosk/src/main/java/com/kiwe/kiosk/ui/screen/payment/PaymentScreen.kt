package com.kiwe.kiosk.ui.screen.payment

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiwe.kiosk.main.MainViewModel
import com.kiwe.kiosk.ui.screen.order.ShoppingCartViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun PaymentScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    viewModel: PaymentViewModel = hiltViewModel(),
    shoppingCartViewModel: ShoppingCartViewModel = hiltViewModel(),
    onCompletePayment: (String) -> Unit = {},
    onEnterScreen: (Int) -> Unit = {},
) {
    val paymentState = viewModel.collectAsState().value
    val context = LocalContext.current
    val shoppingCartState = shoppingCartViewModel.collectAsState().value
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is PaymentSideEffect.Toast -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            }

            is PaymentSideEffect.NavigateToReceiptScreen -> {
                onCompletePayment(sideEffect.orderNumber)
                shoppingCartViewModel.onClearAllItem()
            }
        }
    }
    val pagerState =
        rememberPagerState(pageCount = {
            PaymentStatus.entries.size
        })
    LaunchedEffect(Unit) {
        onEnterScreen(2)
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
                    mainViewModel = mainViewModel,
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
