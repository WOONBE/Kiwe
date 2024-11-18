package com.kiwe.manager.ui.dashboard

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiwe.manager.R
import com.kiwe.manager.ui.component.CenterAlignedTable
import com.kiwe.manager.ui.component.DashBoardAnalytics
import com.kiwe.manager.ui.component.DashBoardCard
import com.kiwe.manager.ui.component.OrderItem
import com.kiwe.manager.ui.theme.Typography
import org.orbitmvi.orbit.compose.collectAsState
import java.util.Locale

private const val TAG = "DashBoardScreen_싸피"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashBoardScreen(viewModel: DashBoardViewModel = hiltViewModel()) {
    val state = viewModel.collectAsState().value
    val orderIncreaseRate =
        state.totalOrderRecent6Month.toList().let {
            if (it.isNotEmpty() && it[1].second != 0) {
                (it[0].second - it[1].second).toDouble() / it[1].second * 100
            } else {
                (-9999).toDouble()
            }
        }

    val saleIncreaseRate =
        state.totalSalesRecent6Month.toList().let {
            if (it.isNotEmpty() && it[1].second != 0) {
                (it[0].second - it[1].second).toDouble() / it[1].second * 100
            } else {
                (-9999).toDouble()
            }
        }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
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
                    .fillMaxSize()
                    .padding(vertical = 20.dp, horizontal = 10.dp),
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "OverView",
                        style = Typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                    var expanded by remember { mutableStateOf(false) }
                    val items = listOf("한 달")
                    var selectedItem by remember { mutableStateOf(items[0]) }

                    Box {
                        // 버튼 클릭 시 메뉴를 열거나 닫음
                        Button(
                            modifier = Modifier.width(100.dp),
                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = Color.Black,
                                ),
                            onClick = { expanded = !expanded },
                        ) {
                            Text(text = selectedItem)
                            Icon(
                                Icons.Rounded.ArrowDropDown,
                                "",
                            )
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                        ) {
                            items.forEach { item ->
                                DropdownMenuItem(
                                    onClick = {
                                        selectedItem = item
                                        expanded = false
                                    },
                                    text = { Text(item) },
                                )
                            }
                        }
                    }
                }

                Row {
                    DashBoardCard(
                        modifier = Modifier.weight(1F),
                        R.drawable.order,
                        "이번달 주문",
                        String.format(Locale.getDefault(), "%,d", state.lastMonthOrder),
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

                    Spacer(modifier = Modifier.width(10.dp))

                    DashBoardCard(
                        modifier = Modifier.weight(1F),
                        R.drawable.sale,
                        "이번달 판매 매출",
                        "₩ ${String.format(Locale.getDefault(), "%,d원", state.lastMonthSale)}",
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
                        if (saleIncreaseRate >= 0) {
                            "${saleIncreaseRate.toInt()}% 매출이증가했습니다!"
                        } else if (saleIncreaseRate.toInt() == -9999) {
                            ""
                        } else {
                            "${saleIncreaseRate.toInt()}% 매출이 감소했습니다"
                        },
                    )
                    Spacer(modifier = Modifier.width(10.dp))

                    DashBoardCard(
                        modifier = Modifier.weight(1F),
                        R.drawable.sale,
                        "이번달 예상 수익",
                        "₩ ${String.format(Locale.getDefault(), "%,d원", (state.lastMonthSale * 0.4).toInt())}",
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
                        if (saleIncreaseRate >= 0) {
                            "${saleIncreaseRate.toInt()}% 수익이증가했습니다!"
                        } else if (saleIncreaseRate.toInt() == -9999) {
                            ""
                        } else {
                            "${saleIncreaseRate.toInt()}% 수익이 감소했습니다"
                        },
                    )
                    Spacer(modifier = Modifier.width(10.dp))

                    DashBoardCard(
                        modifier = Modifier.weight(1F),
                        R.drawable.rating,
                        "매장 평점 (Google Maps)",
                        "4.7/5",
                        R.drawable.increase,
                        R.color.dashboard_card_increase,
                        "2% 평점이 증가했습니다",
                    )
                }

                Spacer(modifier = Modifier.height(50.dp))
                val tableData =
                    state.topSellingMenusSortByAge.map {
                        listOf(
                            it.key,
                            it.value.entries
                                .first()
                                .key,
                            it.value.entries
                                .first()
                                .value
                                .toString(),
                        )
                    }
                Log.d(TAG, "DashBoardScreen: $tableData")
                Row {
                    DashBoardAnalytics(
                        modifier = Modifier.weight(1F),
                        buttonModifier = Modifier.width(120.dp),
                        dropDownMenuFirst = listOf("전체"),
                        dropDownMenuSecond = listOf("나이 순"),
                        title = "연령대별 인기 메뉴",
                    ) {
                        val data =
                            listOf(
                                listOf("나이", "제품명", "판매량"),
                            ) +
                                state.topSellingMenusSortByAge.map {
                                    listOf(
                                        it.key,
                                        it.value.entries
                                            .first()
                                            .key,
                                        it.value.entries
                                            .first()
                                            .value
                                            .toString(),
                                    )
                                }

                        CenterAlignedTable(data)
                    }

                    Spacer(modifier = Modifier.width(50.dp))

                    DashBoardAnalytics(
                        modifier = Modifier.weight(1F),
                        buttonModifier = Modifier.width(100.dp),
                        dropDownMenuFirst = listOf("전체"),
                        title = "판매량 기반 추천메뉴 구성",
                    ) {
                        val firstMenuList =
                            if (state.listMenuSuggested.chunked(6).isNotEmpty()) {
                                state.listMenuSuggested.chunked(6)[0]
                            } else {
                                emptyList()
                            }
                        val secondMenuList =
                            if (state.listMenuSuggested.chunked(6).size >= 2) {
                                state.listMenuSuggested.chunked(6)[1]
                            } else {
                                emptyList()
                            }

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceAround,
                        ) {
                            if (firstMenuList.isNotEmpty()) {
                                item {
                                    Row(
                                        modifier = Modifier.padding(18.dp),
                                    ) {
                                        firstMenuList.forEach {
                                            OrderItem(
                                                orderItem =
                                                it,
                                                modifier = Modifier.weight(1F),
                                            ) { _, _, _, _ ->
                                            }
                                        }
                                        repeat(6 - firstMenuList.size) {
                                            Spacer(modifier = Modifier.weight(1F))
                                        }
                                    }
                                }
                                if (secondMenuList.isNotEmpty()) {
                                    item {
                                        Row(
                                            modifier = Modifier.padding(18.dp),
                                        ) {
                                            secondMenuList.forEach {
                                                OrderItem(
                                                    orderItem =
                                                    it,
                                                    modifier = Modifier.weight(1F),
                                                ) { _, _, _, _ ->
                                                }
                                            }
                                            repeat(6 - secondMenuList.size) {
                                                Spacer(modifier = Modifier.weight(1F))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(device = "spec: width=2304dp, height=1440dp")
@Composable
private fun DashBoardScreenPreview() {
    DashBoardScreen()
}
