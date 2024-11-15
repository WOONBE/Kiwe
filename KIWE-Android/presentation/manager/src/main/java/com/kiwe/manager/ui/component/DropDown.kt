package com.kiwe.manager.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun DropDown(
    modifier: Modifier,
    expanded: Boolean,
    selectedItem: String,
    menu: List<String>,
    onButtonClick: () -> Unit,
    onItemClick: (String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    Box {
        // 버튼 클릭 시 메뉴를 열거나 닫음
        Button(
            modifier = modifier,
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black,
                ),
            onClick = onButtonClick,
        ) {
            Text(text = selectedItem)
            Icon(
                Icons.Rounded.ArrowDropDown,
                "",
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
        ) {
            menu.forEach { item ->
                DropdownMenuItem(
                    onClick = { onItemClick(item) },
                    text = { Text(item) },
                )
            }
        }
    }
}
