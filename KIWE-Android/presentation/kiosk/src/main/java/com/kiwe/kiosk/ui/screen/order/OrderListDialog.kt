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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiwe.kiosk.R
import com.kiwe.kiosk.model.ShoppingCartItem
import com.kiwe.kiosk.ui.screen.order.component.OrderDialog
import com.kiwe.kiosk.ui.theme.Typography
import org.orbitmvi.orbit.compose.collectAsState
import java.util.Locale

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
                        var title = it.menuTitle
                        it.menuRadioOption.forEach { option ->
                            title += "\n${option.key} : ${option.value.first}"
                        }
                        listOf(
                            title,
                            it.count.toString(),
                            it.totalPrice.toString(),
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
                        var menuPrice = shoppingCartItem.defaultPrice
                        shoppingCartItem.menuRadioOption.forEach {
                            menuPrice += it.value.second
                        }
                        cost += shoppingCartItem.count * menuPrice
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
        tableHeader.forEachIndexed { index, text ->
            when (index) {
                0 -> {
                    Text(
                        text = text,
                        modifier =
                            Modifier
                                .weight(5f)
                                .padding(4.dp),
                        color = Color.Black,
                        style = Typography.titleSmall,
                        textAlign = TextAlign.Center,
                    )
                }

                1 -> {
                    Text(
                        text = text,
                        modifier =
                            Modifier
                                .weight(2f)
                                .padding(4.dp),
                        color = Color.Black,
                        style = Typography.titleSmall,
                        textAlign = TextAlign.Center,
                    )
                }

                else -> {
                    Text(
                        text = text,
                        modifier =
                            Modifier
                                .weight(3f)
                                .padding(4.dp),
                        color = Color.Black,
                        style = Typography.titleSmall,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
fun TableBody(tableBody: List<String>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        tableBody.forEachIndexed { index, data ->
            when (index) {
                0 -> {
                    val parts = data.split("\n")
                    val title = parts.firstOrNull() ?: ""
                    val option = parts.drop(1).joinToString("\n")
                    Text(
                        text =
                            buildAnnotatedString {
                                withStyle(
                                    style =
                                        SpanStyle(
                                            color = Color.Black,
                                            fontFamily = FontFamily(Font(R.font.pretendard_extrabold)),
                                            fontSize = 16.sp,
                                            letterSpacing = (-0.03).em,
                                        ),
                                ) {
                                    append(title)
                                }
                                if (option.isNotEmpty()) {
                                    append("\n")
                                }
                                withStyle(
                                    style =
                                        SpanStyle(
                                            color = Color.Black,
                                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                            fontSize = 12.sp,
                                            letterSpacing = (-0.03).em,
                                        ),
                                ) {
                                    append(option)
                                }
                            },
                        modifier =
                            Modifier
                                .weight(5f)
                                .padding(8.dp),
                        color = Color.Black,
                        style = Typography.labelSmall,
                        textAlign = TextAlign.Center,
                    )
                }

                2 -> {
                    Text(
                        text =
                            String.format(Locale.getDefault(), "%,d", data.toInt()),
                        modifier =
                            Modifier
                                .weight(3F)
                                .padding(8.dp),
                        color = Color.Black,
                        style = Typography.bodySmall,
                        textAlign = TextAlign.Center,
                    )
                }

                else -> {
                    Text(
                        text = data,
                        modifier =
                            Modifier
                                .weight(2f)
                                .padding(8.dp),
                        color = Color.Black,
                        style = Typography.bodySmall,
                        textAlign = TextAlign.Center,
                    )
                }
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
        items(data.drop(1).size) {
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
//                        menuPrice = 4500,
//                        menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                        count = 2,
                    ),
                    ShoppingCartItem(
                        menuTitle = "콜드브루디카페인",
//                        menuPrice = 3500,
//                        menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                        count = 1,
                    ),
                    ShoppingCartItem(
                        menuTitle = "딸기쿠키프라페",
//                        menuPrice = 5000,
//                        menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                        count = 99,
                    ),
                    ShoppingCartItem(
                        menuTitle = "딸기쿠키프라페11111",
//                        menuPrice = 5000,
//                        menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                        count = 99,
                    ),
                    ShoppingCartItem(
                        menuTitle = "딸111기쿠키프라페1111",
//                        menuPrice = 5000,
//                        menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                        count = 99,
                    ),
                    ShoppingCartItem(
                        menuTitle = "딸111기쿠키프라페111",
//                        menuPrice = 5000,
//                        menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                        count = 99,
                    ),
                    ShoppingCartItem(
                        menuTitle = "딸111기쿠키프라페11",
//                        menuPrice = 5000,
//                        menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                        count = 99,
                    ),
                    ShoppingCartItem(
                        menuTitle = "딸111기쿠키프라페1",
//                        menuPrice = 5000,
//                        menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                        count = 99,
                    ),
                ),
            ),
        {},
    )
}
