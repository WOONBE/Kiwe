package com.kiwe.manager.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.kiwe.manager.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    //    @Inject
    //    lateinit var getTokenUseCase: GetTokenUseCase
//    private var waiting = true

    override fun onCreate(savedInstanceState: Bundle?) {
        // SplashScreen 초기화
        installSplashScreen().apply {
            // 특정 조건 만족할 때 동안 Splash 화면이 유지되도록
//            setKeepOnScreenCondition {
//                waiting
//            }
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        startActivity(
            Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            },
        )
    }
}
