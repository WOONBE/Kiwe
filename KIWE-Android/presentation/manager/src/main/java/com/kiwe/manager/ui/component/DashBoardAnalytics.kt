package com.kiwe.manager.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiwe.manager.R

@Composable
fun DashBoardAnalytics(
    modifier: Modifier,
    buttonModifier: Modifier,
    dropDownMenuFirst: List<String>,
    dropDownMenuSecond: List<String> = emptyList(),
    title: String,
    onFirstDropdownMenuChanged: (String) -> Unit = {},
    onSecondDropdownMenuChanged: (String) -> Unit = {},
    content: @Composable () -> Unit,
) {
    var firstDropDownExpanded by remember { mutableStateOf(false) }
    var secondDropDownExpanded by remember { mutableStateOf(false) }
    var firstDropDownSelectedItem by remember { mutableStateOf(dropDownMenuFirst[0]) }
    var secondDropDownSelectedItem by remember {
        mutableStateOf(
            if (dropDownMenuSecond.isEmpty()) {
                ""
            } else {
                dropDownMenuSecond[0]
            },
        )
    }
    Card(
        modifier = modifier.fillMaxSize(),
        colors =
            CardDefaults.cardColors(
                containerColor = colorResource(R.color.dashboard_card_background),
            ),
        shape = RoundedCornerShape(20.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(title)
            Row {
                DropDown(
                    modifier = buttonModifier,
                    expanded = firstDropDownExpanded,
                    selectedItem = firstDropDownSelectedItem,
                    menu = dropDownMenuFirst,
                    onButtonClick = { firstDropDownExpanded = !firstDropDownExpanded },
                    onItemClick = { item ->
                        onFirstDropdownMenuChanged(item)
                        firstDropDownSelectedItem = item
                        firstDropDownExpanded = false
                    },
                    onDismissRequest = { firstDropDownExpanded = false },
                )

                if (secondDropDownSelectedItem.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(10.dp))
                    DropDown(
                        modifier = buttonModifier,
                        expanded = secondDropDownExpanded,
                        selectedItem = secondDropDownSelectedItem,
                        menu = dropDownMenuSecond,
                        onButtonClick = { secondDropDownExpanded = !secondDropDownExpanded },
                        onItemClick = { item ->
                            onSecondDropdownMenuChanged(item)
                            secondDropDownSelectedItem = item
                            secondDropDownExpanded = false
                        },
                        onDismissRequest = { secondDropDownExpanded = false },
                    )
                }
            }
        }
        content()
    }
}

@Preview
@Composable
private fun DashBoardAnalyticsPreview() {
    DashBoardAnalytics(
        modifier = Modifier.padding(1.dp),
        buttonModifier = Modifier.width(100.dp),
        onFirstDropdownMenuChanged = {},
        onSecondDropdownMenuChanged = {},
        dropDownMenuFirst = listOf(),
        title = "faucibus",
        content = { Text("") },
    )
}
