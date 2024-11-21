package com.kiwe.manager.ui.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.kiwe.manager.R
import com.kiwe.manager.ui.component.LoginAppBar
import com.kiwe.manager.ui.theme.KIWEAndroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Surface(
                modifier = Modifier.systemBarsPadding(),
                color = colorResource(R.color.login_dark),
                contentColor = colorResource(R.color.white),
            ) {
                KIWEAndroidTheme {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        LoginAppBar()
                        LoginNavHost()
                    }
                }
            }
        }
    }
}
