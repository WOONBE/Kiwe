package com.kiwe.kiosk.ui.screen.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiwe.kiosk.R
import com.kiwe.kiosk.model.ShoppingCartItem
import com.kiwe.kiosk.ui.screen.order.component.OrderDialog
import com.kiwe.kiosk.ui.theme.Typography
import org.orbitmvi.orbit.compose.collectAsState
import timber.log.Timber
import java.util.Locale

private const val TAG = "OrderListDialog 싸피"

@Composable
fun OrderListDialog(
    viewModel: ShoppingCartViewModel = hiltViewModel(),
    onClose: () -> Unit,
) {
    val state = viewModel.collectAsState().value
    OrderListDialog(state, onClose)
}

@Composable
fun OrderListDialog(
    state: ShoppingCartState,
    onClose: () -> Unit,
) {
    OrderDialog {
        Text(
            modifier = Modifier.padding(vertical = 15.dp),
            textAlign = TextAlign.Center,
            text = "주문내역",
            style = Typography.titleLarge,
        )

        Column(
            modifier =
                Modifier
                    .background(color = colorResource(R.color.KIWE_white_transparent_half)),
        ) {
            CenterAlignedTable(
                listOf(
                    listOf("메뉴명", "수량", "금액(원)"),
                ).plus(
                    state.shoppingCartItem.map {
                        listOf(
                            it.menuTitle,
                            it.count.toString(),
                            it.menuPrice.toString(),
                        )
                    },
                ),
            )

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    var cost = 0
                    for (shoppingCartItem in state.shoppingCartItem) {
                        cost += shoppingCartItem.count * shoppingCartItem.menuPrice
                    }
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            "총 ${state.shoppingCartItem.size}건",
                        )
                        Text(
                            text = String.format(Locale.getDefault(), "%,d원", cost),
                        )
                    }
                    Row {
                        Button(
                            modifier = Modifier.weight(1F),
                            shape = RoundedCornerShape(10.dp),
                            onClick = {
                                onClose()
                            },
                            colors =
                                ButtonColors(
                                    contentColor = Color.White,
                                    containerColor = colorResource(R.color.KIWE_gray1),
                                    disabledContainerColor = colorResource(R.color.KIWE_gray1),
                                    disabledContentColor = Color.White,
                                ),
                        ) {
                            Text(
                                text = "닫기",
                                style = Typography.bodyLarge,
                            )
                        }
                        Spacer(modifier = Modifier.weight(0.3F))
                        Button(
                            modifier = Modifier.weight(1F),
                            enabled = state.shoppingCartItem.isNotEmpty(),
                            shape = RoundedCornerShape(10.dp),
                            onClick = {},
                            colors =
                                ButtonColors(
                                    contentColor = Color.White,
                                    containerColor = colorResource(R.color.KIWE_green5),
                                    disabledContainerColor = colorResource(R.color.KIWE_silver1),
                                    disabledContentColor = Color.White,
                                ),
                        ) {
                            Text(
                                text = "결제",
                                style = Typography.bodyLarge,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TableHeader(tableHeader: List<String>) {
    Row {
        tableHeader.forEach {
            Text(
                text = it,
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(4.dp),
                color = Color.Black,
                style = Typography.titleSmall,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun TableBody(tableBody: List<String>) {
    Timber.tag(TAG).d("TableBody: ${tableBody.size}")
    Row {
        tableBody.forEachIndexed { index, data ->
            if (index == 2) {
                Text(
                    text =
                        String.format(Locale.getDefault(), "%,d", data.toInt()),
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(8.dp),
                    color = Color.Black,
                    style = Typography.bodySmall,
                    textAlign = TextAlign.Center,
                )
            } else {
                Text(
                    text = data,
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(8.dp),
                    color = Color.Black,
                    style = Typography.bodySmall,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
fun CenterAlignedTable(data: List<List<String>>) {
    LazyColumn(
        modifier =
            Modifier
                .height(
                    LocalConfiguration.current.screenHeightDp.dp * 0.6F,
                ).padding(horizontal = 15.dp),
    ) {
        items(1) {
            TableHeader(data[0])
            HorizontalDivider(thickness = 2.dp, color = Color.Black)
        }
        Timber.tag(TAG).d("CenterAlignedTable: $data")
        items(data.drop(1).size) {
            Timber.tag(TAG).d("CenterAlignedTable: ${data.drop(1)[it]}")
            TableBody(data.drop(1)[it])
            HorizontalDivider(thickness = 0.5.dp, color = Color.Black)
        }
    }
}

@Preview
@Composable
fun OrderListDialogPreview() {
    OrderListDialog(
        state =
            ShoppingCartState(
                listOf(
                    ShoppingCartItem(
                        menuTitle = "디카페인 카페모카",
                        menuPrice = 4500,
                        menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                        count = 2,
                        option = mapOf("샷" to "연하게", "당도" to "추가"),
                    ),
                    ShoppingCartItem(
                        menuTitle = "콜드브루디카페인",
                        menuPrice = 3500,
                        menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                        count = 1,
                        option = mapOf("샷" to "연하게"),
                    ),
                    ShoppingCartItem(
                        menuTitle = "딸기쿠키프라페",
                        menuPrice = 5000,
                        menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                        count = 99,
                        option = mapOf("당도" to "추가"),
                    ),
                    ShoppingCartItem(
                        menuTitle = "딸기쿠키프라페11111",
                        menuPrice = 5000,
                        menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                        count = 99,
                        option = mapOf("당도" to "추가"),
                    ),
                    ShoppingCartItem(
                        menuTitle = "딸111기쿠키프라페1111",
                        menuPrice = 5000,
                        menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                        count = 99,
                        option = mapOf("당도" to "추가", "샷" to "연하게", "얼음" to "추가", "테이크 아웃" to "O"),
                    ),
                    ShoppingCartItem(
                        menuTitle = "딸111기쿠키프라페111",
                        menuPrice = 5000,
                        menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                        count = 99,
                        option = mapOf("당도" to "추가", "샷" to "연하게", "얼음" to "추가", "테이크 아웃" to "O"),
                    ),
                    ShoppingCartItem(
                        menuTitle = "딸111기쿠키프라페11",
                        menuPrice = 5000,
                        menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                        count = 99,
                        option = mapOf("당도" to "추가", "샷" to "연하게", "얼음" to "추가", "테이크 아웃" to "O"),
                    ),
                    ShoppingCartItem(
                        menuTitle = "딸111기쿠키프라페1",
                        menuPrice = 5000,
                        menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                        count = 99,
                    ),
                ),
            ),
        {},
    )
}
