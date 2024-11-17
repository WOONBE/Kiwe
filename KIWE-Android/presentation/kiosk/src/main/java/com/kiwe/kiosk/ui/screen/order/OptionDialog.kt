package com.kiwe.kiosk.ui.screen.order

import android.view.Gravity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import coil.compose.AsyncImage
import com.kiwe.domain.model.MenuCategoryParam
import com.kiwe.kiosk.R
import com.kiwe.kiosk.model.OrderOption
import com.kiwe.kiosk.model.ShoppingCartItem
import com.kiwe.kiosk.ui.screen.order.component.OptionListItem
import com.kiwe.kiosk.ui.screen.utils.prefixingImagePaths
import com.kiwe.kiosk.ui.theme.Typography
import org.orbitmvi.orbit.compose.collectAsState
import java.util.Locale

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
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false,
            ),
    ) {
        val dialogWindowProvider = LocalView.current.parent as DialogWindowProvider
        dialogWindowProvider.window.setGravity(Gravity.BOTTOM)
        OptionDialog(
            state,
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
            text = state.menuItem.name,
            color = Color.Black,
        )
        AsyncImage(
            modifier =
                Modifier
                    .size(200.dp)
                    .aspectRatio(1F),
            model = state.menuItem.imgPath.prefixingImagePaths(),
            contentDescription = "메뉴 이미지",
        )
        Spacer(Modifier.height(5.dp))
        Text(
            modifier = Modifier.padding(horizontal = 10.dp),
            text = state.menuItem.description,
            style = Typography.bodyMedium.copy(fontSize = 10.sp),
        )
        Spacer(Modifier.height(5.dp))
        Text(
            text = String.format(Locale.getDefault(), "%,d원", state.menuItem.price),
            style = Typography.bodyMedium,
        )
        state.optionList.forEach {
            OptionListItem(it.key, it.value, onRadioOptionClick) {
                onChange()
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
            val totalPrice = String.format(Locale.getDefault(), "%,d원", state.totalPrice * state.menuCount)

            Text(
                text = totalPrice,
                style = Typography.bodyMedium,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                IconButton(
                    onClick = {
                        if (state.menuCount > 1) {
                            onMinus()
                        }
                    },
                    modifier = Modifier.size(28.dp), // 버튼 크기를 조정하여 클릭 영역 관리
                ) {
                    Icon(
                        painter = painterResource(R.drawable.minus),
                        contentDescription = "감소 버튼",
                        tint = Color.Unspecified, // 원본 색상을 유지
                    )
                }

                Text(
                    modifier = Modifier.padding(horizontal = 15.dp),
                    text = state.menuCount.toString(),
                    style = Typography.bodyMedium,
                )

                IconButton(
                    onClick = {
                        if (state.menuCount < 99) {
                            onPlus()
                        }
                    },
                    modifier = Modifier.size(28.dp), // 버튼 크기를 조정하여 클릭 영역 관리
                ) {
                    Icon(
                        painter = painterResource(R.drawable.plus),
                        contentDescription = "증가 버튼",
                        tint = Color.Unspecified, // 원본 색상을 유지
                    )
                }
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
                            menuId = state.menuItem.id,
                            menuImgPath = state.menuItem.imgPath,
                            menuTitle = state.menuItem.name,
                            menuRadioOption = state.radioOptionCost,
                            defaultPrice = state.menuItem.price,
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
                menuItem = MenuCategoryParam(),
                optionList =
                    mutableMapOf<String, List<OrderOption>>().apply {
                        this["샷 추가"] =
                            listOf(
                                OrderOption(
                                    optionImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                                    title = "1샷 추가",
                                    optionImgRes = null,
                                    price = 500,
                                    radio = true,
                                ),
                                OrderOption(
                                    optionImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                                    title = "",
                                    optionImgRes = null,
                                    price = 0,
                                    radio = true,
                                ),
                            )

                        this["설탕 추가"] =
                            listOf(
                                OrderOption(
                                    optionImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                                    optionImgRes = null,
                                    title = "1개 추가",
                                    price = 100,
                                    radio = true,
                                ),
                                OrderOption(
                                    optionImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                                    optionImgRes = null,
                                    title = "2개",
                                    price = 200,
                                    radio = true,
                                ),
                            )
                    },
            ),
        onChange = {},
        onPlus = {},
        onMinus = {},
        onClose = {},
        onInsert = {},
        onRadioOptionClick = { _, _, _ -> },
        onPurchase = {},
    )
}
