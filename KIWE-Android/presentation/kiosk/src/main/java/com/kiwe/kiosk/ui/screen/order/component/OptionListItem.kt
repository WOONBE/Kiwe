package com.kiwe.kiosk.ui.screen.order.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiwe.kiosk.R
import com.kiwe.kiosk.model.OrderOption

@Composable
fun OptionListItem(
    optionCategory: String,
    orderOptionList: List<OrderOption>,
    onRadioOptionClick: (String, String, Int) -> Unit,
    onOptionChange: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 15.dp),
            text = optionCategory,
        )

        Row(
            Modifier
                .fillMaxWidth()
                .background(
                    color = colorResource(R.color.KIWE_silver1),
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (orderOptionList.first().radio) {
                var selectedOption by remember { mutableIntStateOf(-1) }
                OptionListItem(
                    selectedOption,
                    onRadioOptionClick = { index, optionTitle, optionPrice ->
                        selectedOption = index
                        onRadioOptionClick(optionCategory, optionTitle, optionPrice)
                        onOptionChange()
                    },
                    orderOptionList,
                )
            }
        }
    }
}

@Composable
fun OptionListItem(
    selectedOption: Int,
    onRadioOptionClick: (Int, String, Int) -> Unit,
    orderOptionList: List<OrderOption>,
) {
    orderOptionList.forEachIndexed { index, orderOption ->
        Box(
            modifier =
                Modifier.padding(10.dp),
        ) {
            OptionItem(
                optionName = orderOption.title,
                optionPrice = orderOption.price,
                optionImg = orderOption.optionImgUrl,
                selected = (index == selectedOption),
                onRadioOptionClick = {
                    onRadioOptionClick(
                        index,
                        orderOption.title,
                        orderOption.price,
                    )
                },
            )
        }
    }
}

@Preview
@Composable
fun OptionListItemPreview() {
    OptionListItem(
        "askak",
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
        ),
        { _, _, _ -> },
        {},
    )
}
