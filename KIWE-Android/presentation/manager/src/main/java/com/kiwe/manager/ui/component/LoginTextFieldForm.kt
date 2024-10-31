package com.kiwe.manager.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun LoginTextFieldForm(
    title: String,
    value: String,
    onValueChange: (data: String) -> Unit,
    placeHolderText: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    imageVector: ImageVector? = null,
    onIconClick: () -> Unit = {}
) {
    Text(text = title)
    LoginTextField(
        value = value,
        onValueChange = onValueChange,
        placeHolderText = placeHolderText,
        visualTransformation = visualTransformation,
        imageVector = imageVector,
        onIconClick = onIconClick
    )
}
