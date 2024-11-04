package com.kiwe.kiosk.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kiwe.kiosk.navigation.MainNavHost
import com.kiwe.kiosk.ui.theme.KIWEAndroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KIWEAndroidTheme {
                MainNavHost()
            }
        }
    }
}
