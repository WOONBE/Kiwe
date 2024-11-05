package com.kiwe.kiosk.ui.screen.payment

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiwe.kiosk.main.MainViewModel

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
    var showDialog by remember { mutableStateOf(false) }

    val pagerState =
        rememberPagerState(pageCount = {
            PaymentStatus.entries.size
        })

    HorizontalPager(
        modifier = modifier,
        state = pagerState,
        userScrollEnabled = true, // TODO : 테스트 후 false
    ) { page ->
        when (PaymentStatus.entries[page]) {
            PaymentStatus.TAKEOUT -> {
                TakeOutChoiceScreen(
                    modifier = Modifier,
                    onClick = { showDialog = true },
                )
                if (showDialog) {
                    CardCreditDialog(
                        onDismissRequest = { showDialog = false },
                    )
                }
            }

            PaymentStatus.POINT -> {
                TakeOutChoiceScreen(
                    modifier = Modifier,
                )
            }

            PaymentStatus.CARD -> {
                TakeOutChoiceScreen(
                    modifier = Modifier,
                )
            }
        }
    }
}
