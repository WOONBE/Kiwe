package com.kiwe.kiosk.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowInsetsCompat.Type.systemBars
import com.kiwe.kiosk.navigation.MainNavHost
import com.kiwe.kiosk.ui.theme.KIWEAndroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.insetsController?.apply {
            hide(systemBars())
        }
        setContent {
            KIWEAndroidTheme {
                MainNavHost()
            }
        }
    }
}
