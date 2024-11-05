package com.kiwe.kiosk.ui.screen.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiwe.domain.model.Category
import com.kiwe.kiosk.main.MainViewModel
import com.kiwe.kiosk.ui.screen.menu.component.MenuCategory
import com.kiwe.kiosk.ui.theme.KIWEAndroidTheme
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun MenuScreen(
    viewModel: MainViewModel,
    onCategoryClick: (String, Int) -> Unit,
) {
    val state = viewModel.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.setPage(1)
    }
    MenuScreen(
        category = state.category,
        onCategoryClick = onCategoryClick,
    )
}

@Composable
private fun MenuScreen(
    category: List<Category>,
    onCategoryClick: (String, Int) -> Unit,
) {
    Box(
        modifier =
            Modifier
                .padding(horizontal = 40.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MenuCategory(
                    categoryImage = category[0].categoryImage,
                    categoryName = category[0].categoryName,
                    onCategoryClick = onCategoryClick,
                )
                MenuCategory(
                    categoryImage = category[1].categoryImage,
                    categoryName = category[1].categoryName,
                    onCategoryClick = onCategoryClick,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MenuCategory(
                    categoryImage = category[2].categoryImage,
                    categoryName = category[2].categoryName,
                    onCategoryClick = onCategoryClick,
                )
                MenuCategory(
                    categoryImage = category[3].categoryImage,
                    categoryName = category[3].categoryName,
                    onCategoryClick = onCategoryClick,
                )
            }
        }
    }
}

@Preview
@Composable
fun MenuScreenPreview() {
    KIWEAndroidTheme {
        MenuScreen(emptyList(), onCategoryClick = { _, _ -> })
    }
}
