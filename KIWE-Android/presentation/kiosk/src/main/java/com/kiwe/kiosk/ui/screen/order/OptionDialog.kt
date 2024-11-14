package com.kiwe.kiosk.ui.screen.order

import android.view.Gravity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.kiwe.kiosk.R
import com.kiwe.kiosk.model.OrderOption
import com.kiwe.kiosk.model.ShoppingCartItem
import com.kiwe.kiosk.ui.screen.order.component.OptionListItem
import com.kiwe.kiosk.ui.theme.Typography
import org.orbitmvi.orbit.compose.collectAsState
import timber.log.Timber

private const val TAG = "OptionDialog 싸피"

@Composable
fun OptionDialog(
    onClose: () -> Unit,
    onInsert: () -> Unit,
    shoppingCartViewModel: ShoppingCartViewModel,
    optionViewModel: OptionViewModel,
) {
    val state = optionViewModel.collectAsState().value
    var change by remember { mutableIntStateOf(0) }
    Dialog(
        onDismissRequest = {},
        properties =
            DialogProperties(
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false,
            ),
    ) {
        val dialogWindowProvider = LocalView.current.parent as DialogWindowProvider
        dialogWindowProvider.window.setGravity(Gravity.BOTTOM)
        OptionDialog(
            state,
            change,
            { change += 1 },
            onPlus = optionViewModel::onPlus,
            onMinus = optionViewModel::onMinus,
            onClose = onClose,
            onInsert = onInsert,
            onRadioOptionClick = optionViewModel::onRadioOptionClick,
            onPurchase = {
                shoppingCartViewModel.onInsertItem(it)
                onClose()
            },
        )
    }
}

@Composable
private fun OptionDialog(
    state: OptionState,
    change: Int,
    onChange: () -> Unit,
    onPlus: () -> Unit,
    onMinus: () -> Unit,
    onInsert: () -> Unit,
    onClose: () -> Unit,
    onRadioOptionClick: (String, String, Int) -> Unit,
    onPurchase: (ShoppingCartItem) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .background(Color.White)
                .padding(vertical = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = state.menuTitle,
            color = Color.Black,
        )
        state.optionList.forEach {
            OptionListItem(it.key, it.value, onRadioOptionClick) {
                onChange()
                Timber.tag(TAG).d("OptionDialog: click $change")
            }
        }

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "${(state.totalPrice * state.menuCount)}원",
                style = Typography.bodyMedium,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Image(
                    painterResource(R.drawable.minus),
                    modifier =
                        Modifier
                            .size(25.dp)
                            .clickable {
                                if (state.menuCount > 1) {
                                    onMinus()
                                }
                            },
                    contentDescription = "감소 버튼",
                )
                Text(
                    modifier =
                        Modifier.padding(horizontal = 15.dp),
                    text = state.menuCount.toString(),
                    style = Typography.bodyMedium,
                )
                Image(
                    painterResource(R.drawable.plus),
                    modifier =
                        Modifier
                            .size(25.dp)
                            .clickable {
                                onPlus()
                            },
                    contentDescription = "증가 버튼",
                )
            }
        }

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp),
        ) {
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
                shape = RoundedCornerShape(10.dp),
                onClick = {
                    onPurchase(
                        ShoppingCartItem(
                            menuId = state.menuId,
                            menuImgPath = state.menuImgPath,
                            menuTitle = state.menuTitle,
                            menuRadioOption = state.radioOptionCost,
                            defaultPrice = state.menuCost,
                            count = state.menuCount,
                        ),
                    )
                    onInsert()
                },
                colors =
                    ButtonColors(
                        contentColor = Color.White,
                        containerColor = colorResource(R.color.KIWE_green5),
                        disabledContainerColor = colorResource(R.color.KIWE_silver1),
                        disabledContentColor = Color.White,
                    ),
            ) {
                Text(
                    text = "담기",
                    style = Typography.bodyLarge,
                )
            }
        }
    }
}

@Preview
@Composable
private fun OptionDialogPreview() {
    OptionDialog(
        state =
            OptionState(
                menuId = 1,
                menuImgPath = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                menuTitle = "",
                menuCost = 0,
                optionList =
                    mutableMapOf<String, List<OrderOption>>().apply {
                        this["샷 추가"] =
                            listOf(
                                OrderOption(
                                    optionImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                                    title = "1샷 추가",
                                    price = 500,
                                    radio = true,
                                ),
                                OrderOption(
                                    optionImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                                    title = "",
                                    price = 0,
                                    radio = true,
                                ),
                            )

                        this["설탕 추가"] =
                            listOf(
                                OrderOption(
                                    optionImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                                    title = "1개 추가",
                                    price = 100,
                                    radio = true,
                                ),
                                OrderOption(
                                    optionImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                                    title = "2개",
                                    price = 200,
                                    radio = true,
                                ),
                            )

                        this["추가 선택"] =
                            listOf(
                                OrderOption(
                                    optionImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                                    title = "기타1",
                                    price = 0,
                                    radio = false,
                                ),
                                OrderOption(
                                    optionImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                                    title = "기타2",
                                    price = 0,
                                    radio = false,
                                ),
                            )

                        this["옵션2"] =
                            listOf(
                                OrderOption(
                                    optionImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                                    title = "테이크 아웃",
                                    price = -500,
                                    radio = true,
                                ),
                                OrderOption(
                                    optionImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                                    title = "매장에서",
                                    price = 0,
                                    radio = true,
                                ),
                            )
                    },
            ),
        change = 0,
        onChange = {},
        onPlus = {},
        onMinus = {},
        onClose = {},
        onInsert = {},
        onRadioOptionClick = { _, _, _ -> },
        onPurchase = {},
    )
}
