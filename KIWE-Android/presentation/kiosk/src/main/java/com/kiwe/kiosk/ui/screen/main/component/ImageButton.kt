package com.kiwe.kiosk.ui.screen.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.kiwe.kiosk.R
import com.kiwe.kiosk.ui.theme.Typography

@Composable
fun ImageButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: Int,
    color: Int,
) {
    Button(
        colors =
            ButtonDefaults.buttonColors(
                containerColor = colorResource(color),
                contentColor = colorResource(R.color.white),
            ),
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(0.dp),
        onClick = {
        },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.background(Color.Transparent).size(30.dp),
                painter = painterResource(icon),
                contentDescription = "",
            )
            Text(
                text = text,
                style = Typography.titleSmall,
            )
        }
    }
}

@Preview
@Composable
fun ImageButtonPreview() {
    ImageButton(Modifier, "ss", R.drawable.shopping_cart, R.color.KIWE_gray1)
}
