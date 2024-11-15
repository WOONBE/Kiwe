package com.kiwe.manager.ui.dashboard

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiwe.domain.model.MenuCategoryParam
import com.kiwe.domain.model.OrderItem
import com.kiwe.manager.R
import com.kiwe.manager.ui.component.CenterAlignedTable
import com.kiwe.manager.ui.component.DashBoardAnalytics
import com.kiwe.manager.ui.component.DashBoardCard
import com.kiwe.manager.ui.component.OrderItem
import com.kiwe.manager.ui.theme.Typography
import org.orbitmvi.orbit.compose.collectAsState
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashBoardScreen(viewModel: DashBoardViewModel = hiltViewModel()) {
    val state = viewModel.collectAsState().value

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(colorResource(R.color.dashboard_background)),
    ) {
        Text(
            modifier = Modifier.padding(vertical = 20.dp, horizontal = 10.dp),
            text = "xx커피 ㅌㅌ점",
            style = Typography.headlineLarge,
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
                    )
                    var expanded by remember { mutableStateOf(false) }
                    val items = listOf("1일", "일주일", "한 달")
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
                        "1,342",
                        R.drawable.decrease,
                        R.color.dashboard_card_decrease,
                        "10% 매출이 감소했습니다",
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    DashBoardCard(
                        modifier = Modifier.weight(1F),
                        R.drawable.sale,
                        "이번달 판매 매출",
                        "₩ ${String.format(Locale.getDefault(), "%,d원", state.lastMonthIncome)}",
                        R.drawable.increase,
                        R.color.dashboard_card_increase,
                        "12% 매출이 증가했습니다",
                    )
                    Spacer(modifier = Modifier.width(10.dp))

                    DashBoardCard(
                        modifier = Modifier.weight(1F),
                        R.drawable.sale,
                        "이번달 예상 수익",
                        "₩ 4,975,800",
                        R.drawable.increase,
                        R.color.dashboard_card_increase,
                        "12% 수익이 증가했습니다",
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

                Row {
                    DashBoardAnalytics(
                        modifier = Modifier.weight(1F),
                        buttonModifier = Modifier.width(100.dp),
                        dropDownMenuFirst = listOf("음료", "디저트"),
                        dropDownMenuSecond = listOf("음료", "디저트"),
                        title = "연령대별 인기 메뉴",
                    ) {
                        val data =
                            listOf(
                                listOf("나이", "나이", "나이"),
                                listOf("10대", "자바 칩 프라푸치노", "121"),
                                listOf("10대", "자바 칩 프라푸치노", "121"),
                                listOf("10대", "자바 칩 프라푸치노", "121"),
                                listOf("10대", "자바 칩 프라푸치노", "121"),
                                listOf("10대", "자바 칩 프라푸치노", "121"),
                                listOf("10대", "자바 칩 프라푸치노", "121"),
                            )

                        CenterAlignedTable(data)
                    }

                    Spacer(modifier = Modifier.width(50.dp))

                    DashBoardAnalytics(
                        modifier = Modifier.weight(1F),
                        buttonModifier = Modifier.width(100.dp),
                        dropDownMenuFirst = listOf("음료", "디저트"),
                        title = "판매량 기반 추천메뉴 구성",
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceAround,
                        ) {
                            Row(
                                modifier = Modifier.padding(18.dp),
                            ) {
                                OrderItem(
                                    orderItem =
                                        MenuCategoryParam(
                                            id = 1,
                                            category = "디카페인",
                                            categoryNumber = 1,
                                            hotOrIce = "HOT",
                                            name = "디카페인 아메리카노",
                                            price = 1000,
                                            description = "향과 풍미 그대로 카페인만을 낮춰 민감한 분들도 안심하고 매일매일 즐길 수 있는 디카페인 커피",
                                            imgPath = "drinks/딸기주스.jpg",
                                        ),
                                    modifier = Modifier.weight(1F),
                                ) { _, _, _, _ ->
                                }
                                OrderItem(
                                    orderItem =
                                        MenuCategoryParam(
                                            id = 1,
                                            category = "디카페인",
                                            categoryNumber = 1,
                                            hotOrIce = "HOT",
                                            name = "디카페인 아메리카노",
                                            price = 1000,
                                            description = "향과 풍미 그대로 카페인만을 낮춰 민감한 분들도 안심하고 매일매일 즐길 수 있는 디카페인 커피",
                                            imgPath = "drinks/딸기주스.jpg",
                                        ),
                                    modifier = Modifier.weight(1F),
                                ) { _, _, _, _ ->
                                }
                                OrderItem(
                                    orderItem =
                                        MenuCategoryParam(
                                            id = 1,
                                            category = "디카페인",
                                            categoryNumber = 1,
                                            hotOrIce = "HOT",
                                            name = "디카페인 아메리카노",
                                            price = 1000,
                                            description = "향과 풍미 그대로 카페인만을 낮춰 민감한 분들도 안심하고 매일매일 즐길 수 있는 디카페인 커피",
                                            imgPath = "drinks/딸기주스.jpg",
                                        ),
                                    modifier = Modifier.weight(1F),
                                ) { _, _, _, _ ->
                                }
                                OrderItem(
                                    orderItem =
                                        MenuCategoryParam(
                                            id = 1,
                                            category = "디카페인",
                                            categoryNumber = 1,
                                            hotOrIce = "HOT",
                                            name = "디카페인 아메리카노",
                                            price = 1000,
                                            description = "향과 풍미 그대로 카페인만을 낮춰 민감한 분들도 안심하고 매일매일 즐길 수 있는 디카페인 커피",
                                            imgPath = "drinks/딸기주스.jpg",
                                        ),
                                    modifier = Modifier.weight(1F),
                                ) { _, _, _, _ ->
                                }
                                OrderItem(
                                    orderItem =
                                        MenuCategoryParam(
                                            id = 1,
                                            category = "디카페인",
                                            categoryNumber = 1,
                                            hotOrIce = "HOT",
                                            name = "디카페인 아메리카노",
                                            price = 1000,
                                            description = "향과 풍미 그대로 카페인만을 낮춰 민감한 분들도 안심하고 매일매일 즐길 수 있는 디카페인 커피",
                                            imgPath = "drinks/딸기주스.jpg",
                                        ),
                                    modifier = Modifier.weight(1F),
                                ) { _, _, _, _ ->
                                }
                            }

                            Row(
                                modifier = Modifier.padding(18.dp),
                            ) {
                                OrderItem(
                                    orderItem =
                                        MenuCategoryParam(
                                            id = 1,
                                            category = "디카페인",
                                            categoryNumber = 1,
                                            hotOrIce = "HOT",
                                            name = "디카페인 아메리카노",
                                            price = 1000,
                                            description = "향과 풍미 그대로 카페인만을 낮춰 민감한 분들도 안심하고 매일매일 즐길 수 있는 디카페인 커피",
                                            imgPath = "drinks/딸기주스.jpg",
                                        ),
                                    modifier = Modifier.weight(1F),
                                ) { _, _, _, _ ->
                                }
                                OrderItem(
                                    orderItem =
                                        MenuCategoryParam(
                                            id = 1,
                                            category = "디카페인",
                                            categoryNumber = 1,
                                            hotOrIce = "HOT",
                                            name = "디카페인 아메리카노",
                                            price = 1000,
                                            description = "향과 풍미 그대로 카페인만을 낮춰 민감한 분들도 안심하고 매일매일 즐길 수 있는 디카페인 커피",
                                            imgPath = "drinks/딸기주스.jpg",
                                        ),
                                    modifier = Modifier.weight(1F),
                                ) { _, _, _, _ ->
                                }
                                OrderItem(
                                    orderItem =
                                        MenuCategoryParam(
                                            id = 1,
                                            category = "디카페인",
                                            categoryNumber = 1,
                                            hotOrIce = "HOT",
                                            name = "디카페인 아메리카노",
                                            price = 1000,
                                            description = "향과 풍미 그대로 카페인만을 낮춰 민감한 분들도 안심하고 매일매일 즐길 수 있는 디카페인 커피",
                                            imgPath = "drinks/딸기주스.jpg",
                                        ),
                                    modifier = Modifier.weight(1F),
                                ) { _, _, _, _ ->
                                }
                                OrderItem(
                                    orderItem =
                                        MenuCategoryParam(
                                            id = 1,
                                            category = "디카페인",
                                            categoryNumber = 1,
                                            hotOrIce = "HOT",
                                            name = "디카페인 아메리카노",
                                            price = 1000,
                                            description = "향과 풍미 그대로 카페인만을 낮춰 민감한 분들도 안심하고 매일매일 즐길 수 있는 디카페인 커피",
                                            imgPath = "drinks/딸기주스.jpg",
                                        ),
                                    modifier = Modifier.weight(1F),
                                ) { _, _, _, _ ->
                                }
                                OrderItem(
                                    orderItem =
                                        MenuCategoryParam(
                                            id = 1,
                                            category = "디카페인",
                                            categoryNumber = 1,
                                            hotOrIce = "HOT",
                                            name = "디카페인 아메리카노",
                                            price = 1000,
                                            description = "향과 풍미 그대로 카페인만을 낮춰 민감한 분들도 안심하고 매일매일 즐길 수 있는 디카페인 커피",
                                            imgPath = "drinks/딸기주스.jpg",
                                        ),
                                    modifier = Modifier.weight(1F),
                                ) { _, _, _, _ ->
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
