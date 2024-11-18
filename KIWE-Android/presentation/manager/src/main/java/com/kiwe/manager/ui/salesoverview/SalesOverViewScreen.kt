package com.kiwe.manager.ui.salesoverview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiwe.manager.R
import com.kiwe.manager.ui.component.DashBoardCard
import com.kiwe.manager.ui.salesoverview.chart.Chart2
import com.kiwe.manager.ui.theme.Typography
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.util.Locale

@Composable
fun SalesOverviewScreen(viewModel: SalesOverViewViewModel = hiltViewModel()) {
    val state by viewModel.collectAsState()
    val orderIncreaseRate =
        state.totalOrderRecent6Month.toList().let {
            if (it.isNotEmpty() && it[1].second != 0) {
                (it[0].second - it[1].second).toDouble() / it[1].second * 100
            } else {
                (-9999).toDouble()
            }
        }

    val priceIncreaseRate =
        state.totalPriceRecent6Month.toList().let {
            if (it.isNotEmpty() && it[1].second != 0) {
                (it[0].second - it[1].second).toDouble() / it[1].second * 100
            } else {
                (-9999).toDouble()
            }
        }

    viewModel.collectSideEffect {
        when (it) {
            is SalesOverViewSideEffect.Toast -> {
            }

            else -> {
            }
        }
    }
    Column(
        modifier =
            Modifier
                .background(colorResource(R.color.dashboard_background)),
    ) {
        Text(
            modifier = Modifier.padding(vertical = 20.dp, horizontal = 10.dp),
            text = "싸피카페 인동점",
            style = Typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        HorizontalDivider(thickness = 1.dp)
        Box(
            modifier =
                Modifier
                    .weight(1F),
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(vertical = 20.dp, horizontal = 10.dp),
            ) {
                Text(
                    text = "매장 매출 분석",
                    style = Typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(
                    modifier = Modifier.height(20.dp),
                )
                Row(
                    modifier = Modifier.height(IntrinsicSize.Max),
                ) {
                    DashBoardCard(
                        modifier = Modifier.weight(1F).fillMaxHeight(),
                        R.drawable.sale,
                        "이번달 판매 매출",
                        "₩ " +
                            String.format(
                                Locale.getDefault(),
                                "%,d원",
                                state.totalPriceLastMonth,
                            ),
                        if (orderIncreaseRate >= 0) {
                            R.drawable.increase
                        } else if (orderIncreaseRate.toInt() == -9999) {
                            R.drawable.none
                        } else {
                            R.drawable.decrease
                        },
                        if (orderIncreaseRate >= 0) {
                            R.color.dashboard_card_increase
                        } else if (orderIncreaseRate.toInt() == -9999) {
                            R.color.black
                        } else {
                            R.color.dashboard_card_decrease
                        },
                        if (priceIncreaseRate >= 0) {
                            "${priceIncreaseRate.toInt()}% 매출이증가했습니다!"
                        } else if (priceIncreaseRate.toInt() == -9999) {
                            ""
                        } else {
                            "${priceIncreaseRate.toInt()}% 매출이 감소했습니다"
                        },
                    )
                    Spacer(modifier = Modifier.width(10.dp))

                    DashBoardCard(
                        modifier = Modifier.weight(1F).fillMaxHeight(),
                        R.drawable.sale,
                        "이번달 예상 수익",
                        "₩ " +
                            String.format(
                                Locale.getDefault(),
                                "%,d원",
                                (state.totalPriceLastMonth * 0.4).toInt(),
                            ),
                        if (orderIncreaseRate >= 0) {
                            R.drawable.increase
                        } else if (orderIncreaseRate.toInt() == -9999) {
                            R.drawable.none
                        } else {
                            R.drawable.decrease
                        },
                        if (orderIncreaseRate >= 0) {
                            R.color.dashboard_card_increase
                        } else if (orderIncreaseRate.toInt() == -9999) {
                            R.color.black
                        } else {
                            R.color.dashboard_card_decrease
                        },
                        if (orderIncreaseRate >= 0) {
                            "${orderIncreaseRate.toInt()}% 수익이 증가했습니다!"
                        } else if (orderIncreaseRate.toInt() == -9999) {
                            ""
                        } else {
                            "${orderIncreaseRate.toInt()}% 수익이 감소했습니다"
                        },
                    )
                    Spacer(modifier = Modifier.width(10.dp))

                    DashBoardCard(
                        modifier = Modifier.weight(1F).fillMaxHeight(),
                        R.drawable.rating,
                        "이번달 주문 건수",
                        state.totalOrderLastMonth.toString(),
                        if (orderIncreaseRate >= 0) {
                            R.drawable.increase
                        } else if (orderIncreaseRate.toInt() == -9999) {
                            R.drawable.none
                        } else {
                            R.drawable.decrease
                        },
                        if (orderIncreaseRate >= 0) {
                            R.color.dashboard_card_increase
                        } else if (orderIncreaseRate.toInt() == -9999) {
                            R.color.black
                        } else {
                            R.color.dashboard_card_decrease
                        },
                        if (orderIncreaseRate >= 0) {
                            "${orderIncreaseRate.toInt()}% 증가했습니다!"
                        } else if (orderIncreaseRate.toInt() == -9999) {
                            ""
                        } else {
                            "${orderIncreaseRate.toInt()}% 감소했습니다"
                        },
                    )
                }

                Spacer(modifier = Modifier.height(50.dp))
                Row(modifier = Modifier.fillMaxHeight()) {
                    Column(modifier = Modifier.weight(1F)) {
                        Text(
                            "최근 6개월간 매출 추이",
                            style = Typography.titleLarge
                        )
                        if (state.totalPriceRecent6Month.isNotEmpty()) {
                            Chart2(Modifier.fillMaxHeight(), state.totalPriceRecent6Month)
                        }
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Column(
                        Modifier.weight(1F),
                    ) {
                        Text(
                            "최근 6개월간 주문 건수 추이",
                            style = Typography.titleLarge
                        )
                        if (state.totalOrderRecent6Month.isNotEmpty()) {
                            Chart2(Modifier.fillMaxHeight(), state.totalOrderRecent6Month)
                        }
                    }
                }
            }
        }
    }
}

@Preview(device = "spec: width=2304dp, height=1440dp")
@Composable
private fun SalesOverviewScreenPreview() {
    SalesOverviewScreen()
}
