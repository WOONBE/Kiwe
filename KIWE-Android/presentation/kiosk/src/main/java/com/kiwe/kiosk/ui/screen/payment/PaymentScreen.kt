package com.kiwe.kiosk.ui.screen.payment

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kiwe.kiosk.main.MainViewModel
import com.kiwe.kiosk.ui.screen.payment.component.ChoiceScreen

enum class PaymentStatus(
    val page: Int,
) {
    TAKEOUT(0),
    POINT(1),
    CARD(2),
}

@Composable
fun PaymentScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
) {
    val pagerState =
        rememberPagerState(pageCount = {
            PaymentStatus.entries.size
        })

    HorizontalPager(
        modifier = modifier,
        state = pagerState,
        userScrollEnabled = true, // TODO : 테스트 후 false
    ) { page ->
        when(PaymentStatus.entries[page]) {
            PaymentStatus.TAKEOUT -> {
                ChoiceScreen(
                    modifier = Modifier,
                )
            }
            PaymentStatus.POINT -> {
                ChoiceScreen(
                    modifier = Modifier,
                )
            }
            PaymentStatus.CARD -> {
                ChoiceScreen(
                    modifier = Modifier,
                )
            }
        }
    }
}