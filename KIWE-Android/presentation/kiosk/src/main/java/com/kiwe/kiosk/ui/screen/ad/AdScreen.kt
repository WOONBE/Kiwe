package com.kiwe.kiosk.ui.screen.ad

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kiwe.kiosk.ui.theme.KIWEAndroidTheme

@Composable
fun AdScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Ad Screen")
    }
}

@Preview
@Composable
fun AdScreenPreview() {
    KIWEAndroidTheme {
        AdScreen()
    }
}
