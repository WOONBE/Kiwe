package com.kiwe.manager.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiwe.manager.R

@Composable
fun HomeSideBar(
    tabIdx: Int,
    onTabChanged: (Int) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxHeight()
                .background(
                    colorResource(R.color.home_side_bar_background),
                ).padding(top = 150.dp)
                .padding(horizontal = 0.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        DrawerMenuItem(
            icon = ImageVector.vectorResource(R.drawable.dashboard),
            text = "DashBoard",
            selected = tabIdx == 0,
            onClick = { onTabChanged(0) },
        )
        DrawerMenuItem(
            icon = ImageVector.vectorResource(R.drawable.menu),
            text = "메뉴 관리",
            selected = tabIdx == 1,
            onClick = { onTabChanged(1) },
        )
        DrawerMenuItem(
            icon = ImageVector.vectorResource(R.drawable.sales),
            text = "매장 매출 상세",
            selected = tabIdx == 2,
            onClick = { onTabChanged(2) },
        )
        DrawerMenuItem(
            icon = ImageVector.vectorResource(R.drawable.kiosk),
            text = "키오스크 관리",
            selected = tabIdx == 3,
            onClick = { onTabChanged(3) },
        )
        DrawerMenuItem(
            icon = Icons.Outlined.Settings,
            text = "설정",
            selected = tabIdx == 4,
            onClick = { onTabChanged(4) },
        )
    }
}

@Preview
@Composable
fun HomeSideBarPreview() {
    HomeSideBar(
        tabIdx = 0,
        onTabChanged = {},
    )
}
