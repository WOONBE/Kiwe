package com.kiwe.kiosk.ui.screen.payment

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun PaymentScreen(modifier: Modifier = Modifier) {
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

enum class PaymentStatus {
    TAKEOUT,
    POINT,
    CARD,
}
