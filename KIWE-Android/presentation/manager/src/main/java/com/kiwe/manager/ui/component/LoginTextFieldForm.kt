package com.kiwe.manager.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun LoginTextFieldForm(
    title: String,
    value: String,
    onValueChange: (data: String) -> Unit,
    placeHolderText: String
) {
    Text(text = title)
    LoginTextField(
        value = value,
        onValueChange = onValueChange,
        placeHolderText = placeHolderText,
    )
}