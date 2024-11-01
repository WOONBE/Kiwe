package com.kiwe.kiosk.ui.screen.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiwe.domain.model.Category
import com.kiwe.kiosk.main.MainViewModel
import com.kiwe.kiosk.ui.screen.menu.component.MenuCategory
import com.kiwe.kiosk.ui.theme.KIWEAndroidTheme
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun MenuScreen(viewModel: MainViewModel) {
    val state = viewModel.collectAsState().value
    MenuScreen(
        category = state.category,
    )
}

@Composable
private fun MenuScreen(category: List<Category>) {
    Surface(
        modifier =
            Modifier
                .background(Color(0xFFF9F9F9))
                .clip(RoundedCornerShape(30.dp))
                .padding(horizontal = 40.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .background(Color(0xFFF9F9F9))
                    .clip(RoundedCornerShape(30.dp)),
        ) {
            LazyVerticalGrid(
                GridCells.Fixed(2),
                modifier =
                    Modifier
                        .padding(12.dp)
                        .fillMaxSize()
                        .wrapContentSize(),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                items(category.size) { idx ->
                    MenuCategory(
                        categoryImage = category[idx].categoryImage,
                        categoryName = category[idx].categoryName,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun MenuScreenPreview() {
    KIWEAndroidTheme {
        MenuScreen(emptyList())
    }
}
