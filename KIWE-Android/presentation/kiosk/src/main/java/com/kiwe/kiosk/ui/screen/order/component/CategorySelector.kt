package com.kiwe.kiosk.ui.screen.order.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiwe.domain.model.MenuCategoryGroup
import com.kiwe.kiosk.R
import com.kiwe.kiosk.ui.theme.KIWEAndroidTheme
import com.kiwe.kiosk.ui.theme.KiweBlack1
import com.kiwe.kiosk.ui.theme.KiweBrown3
import com.kiwe.kiosk.ui.theme.KiweBrown4
import com.kiwe.kiosk.ui.theme.KiweOrange1
import com.kiwe.kiosk.ui.theme.KiweSilver1
import com.kiwe.kiosk.ui.theme.KiweWhite1
import com.kiwe.kiosk.ui.theme.Typography
import timber.log.Timber

@Composable
fun CategorySelector(
    modifier: Modifier = Modifier,
    categoryState: MenuCategoryGroup = MenuCategoryGroup.NEW,
    onCategoryClick: (MenuCategoryGroup) -> Unit = {},
) {
    val categories = MenuCategoryGroup.entries
    val itemsPerPage = 4
    val pageCount = (categories.size + itemsPerPage - 1) / itemsPerPage
    var currentPage by remember { mutableIntStateOf(categories.indexOf(categoryState) / itemsPerPage) }
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .background(color = KiweSilver1),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        IconButton(
            modifier = Modifier.size(32.dp),
            onClick = { if (currentPage > 0) currentPage-- },
            enabled = currentPage > 0,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_square_left),
                contentDescription = "Previous",
            )
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            val pageCategories = categories.chunked(itemsPerPage)[currentPage]
            val itemCount = pageCategories.size

            pageCategories.forEach { category ->
                val isSelected = category == categoryState
                val backgroundColor = if (isSelected) KiweBrown4 else KiweWhite1
                val textColor = if (isSelected) KiweWhite1 else KiweBlack1
                val textStyle = if (isSelected) Typography.titleMedium else Typography.bodyMedium
                Card(
                    modifier =
                        Modifier
                            .weight(1f)
                            .height(36.dp)
                            .padding(vertical = 2.dp, horizontal = 4.dp),
                    shape = RoundedCornerShape(4.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    onClick = {
                        onCategoryClick(category)
                        Timber.tag(javaClass.simpleName).d("onCategoryClick: $category")
                    },
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .background(color = backgroundColor),
                    ) {
                        Text(
                            text = category.displayName,
                            style = textStyle.copy(fontSize = 12.sp, color = textColor),
                        )
                    }
                }
            }

            // 빈 공간을 채울 Spacer 추가
            repeat(itemsPerPage - itemCount) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        IconButton(
            modifier = Modifier.size(32.dp),
            onClick = { if (currentPage < pageCount - 1) currentPage++ },
            enabled = currentPage < pageCount - 1,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_square_right),
                contentDescription = "Next",
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    KIWEAndroidTheme {
        CategorySelector()
    }
}
