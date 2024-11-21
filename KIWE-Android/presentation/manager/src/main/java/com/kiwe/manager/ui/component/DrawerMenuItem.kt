package com.kiwe.manager.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiwe.manager.R
import com.kiwe.manager.ui.theme.Typography

@Composable
fun DrawerMenuItem(
    icon: ImageVector,
    text: String,
    selected: Boolean = false,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            if (selected) {
                Modifier
                    .padding(horizontal = 20.dp)
                    .width(200.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color.White)
                    .padding(vertical = 10.dp, horizontal = 20.dp)
                    .wrapContentHeight()
                    .clickable { onClick() }
            } else {
                Modifier
                    .padding(horizontal = 20.dp)
                    .width(200.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .padding(vertical = 10.dp, horizontal = 20.dp)
                    .wrapContentHeight()
                    .clickable { onClick() }
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            tint =
                if (selected) {
                    colorResource(R.color.home_side_bar_item_selected)
                } else {
                    colorResource(R.color.white)
                },
            imageVector = icon,
            contentDescription = null,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text,
            color =
                if (selected) {
                    colorResource(R.color.home_side_bar_item_selected)
                } else {
                    colorResource(R.color.white)
                },
            style = Typography.labelLarge,
        )
    }
}

@Preview
@Composable
fun DrawerMenuItemPreview() {
    DrawerMenuItem(Icons.Default.Settings, "세팅", true, {})
}
