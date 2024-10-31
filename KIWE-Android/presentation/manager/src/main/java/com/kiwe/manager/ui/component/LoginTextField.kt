package com.kiwe.manager.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.kiwe.manager.R

@Composable
fun LoginTextField(
    value: String,
    placeHolderText: String,
    onValueChange: (String) -> Unit,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    imageVector: ImageVector?,
    onIconClick: () -> Unit = {}
) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        shape = RoundedCornerShape(12.dp),
        textStyle =
            TextStyle(color = Color.White),
        singleLine = true,
        colors =
            TextFieldDefaults.colors(
                focusedContainerColor = colorResource(R.color.login_text_field_inside), // 포커스된 상태의 배경색
                unfocusedContainerColor = colorResource(R.color.login_text_field_inside), // 포커스되지 않은 상태의 배경색
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
        onValueChange = onValueChange,
        placeholder = { Text(text = placeHolderText) },
        visualTransformation = visualTransformation,
        trailingIcon = {
            imageVector?.let {
                IconButton(
                    onClick = onIconClick
                ) {
                    Icon(
                        imageVector = it,
                        contentDescription = "텍스트 필드 우측 아이콘",
                    )
                }

            }
        },
    )
}
