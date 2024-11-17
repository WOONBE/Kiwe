package com.kiwe.kiosk.ui.screen.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiwe.kiosk.R
import com.kiwe.kiosk.ui.theme.Typography

@Composable
fun ImageButton(
    modifier: Modifier = Modifier,
    headerText: String,
    text: String,
    icon: Int,
    color: Int,
    borderColor: Color = Color.Transparent, // 테두리 색상
    onClick: () -> Unit,
) {
    Button(
        colors =
            ButtonDefaults.buttonColors(
                containerColor = colorResource(color),
                contentColor = colorResource(R.color.white),
            ),
        modifier =
            modifier.border(
                width = 3.dp,
                color = borderColor, // 외부에서 전달받은 애니메이션 테두리 색상
                shape = RoundedCornerShape(10.dp),
            ),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(0.dp),
        onClick = onClick,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier =
                    Modifier
                        .padding(horizontal = 5.dp)
                        .background(Color.Transparent)
                        .size(30.dp),
                painter = painterResource(icon),
                contentDescription = "",
            )
            Column(
                modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (headerText.isNotEmpty()) {
                    Text(
                        text = headerText,
                        style = Typography.bodyMedium.copy(fontSize = 12.sp),
                    )
                }
                Text(
                    text = text,
                    style = Typography.titleSmall.copy(fontSize = 16.sp),
                )
            }
        }
    }
}

@Preview
@Composable
fun ImageButtonPreview() {
    ImageButton(
        Modifier,
        "header",
        "body",
        R.drawable.shopping_cart,
        R.color.KIWE_gray1,
        Color.Red,
        {},
    )
}
