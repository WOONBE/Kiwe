package com.kiwe.kiosk.ui.screen.menu

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiwe.domain.model.Category
import com.kiwe.kiosk.main.MainViewModel
import com.kiwe.kiosk.ui.screen.menu.component.MenuCategory
import com.kiwe.kiosk.ui.screen.utils.rotatedScreenSize
import com.kiwe.kiosk.ui.theme.KIWEAndroidTheme
import com.kiwe.kiosk.ui.theme.KioskBackgroundBrush
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun MenuScreen(
    viewModel: MainViewModel,
    rotationAngle: Float,
    configuration: Configuration,
) {
    val state = viewModel.collectAsState().value
    MenuScreen(
        category = state.category,
        rotationAngle,
        configuration,
    )
}

@Composable
private fun MenuScreen(
    category: List<Category>,
    rotationAngle: Float,
    configuration: Configuration,
) {
    Column(
        modifier =
            Modifier
                .rotatedScreenSize(rotationAngle, configuration)
                .background(KioskBackgroundBrush),
    ) {
        LazyVerticalGrid(
            GridCells.Fixed(2),
            modifier =
                Modifier
                    .fillMaxSize()
                    .wrapContentSize()
                    .padding(20.dp),
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

@Preview
@Composable
fun MenuScreenPreview() {
    KIWEAndroidTheme {
        MenuScreen(emptyList(), 0f, Configuration())
    }
}
