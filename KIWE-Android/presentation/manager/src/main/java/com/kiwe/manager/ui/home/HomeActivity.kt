package com.kiwe.manager.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiwe.manager.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import org.orbitmvi.orbit.compose.collectSideEffect

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Surface {
                HomeScreen(
                    onNavigateToLoginScreen = {
                        startActivity(
                            Intent(
                                this,
                                LoginActivity::class.java,
                            ).apply {
                                flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            },
                        )
                    },
                )
            }
        }
    }
}

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToLoginScreen: () -> Unit,
) {
    val context = LocalContext.current

    viewModel.collectSideEffect {
        when (it) {
            is HomeSideEffect.Toast -> {
                Toast
                    .makeText(
                        context,
                        it.message,
                        Toast.LENGTH_SHORT,
                    ).show()
            }

            HomeSideEffect.NavigateToLoginScreen -> onNavigateToLoginScreen()
        }
    }

    Column {
        Text("Home 화면")
        Button(
            onClick =
                viewModel::onLogout,
        ) {
            Text("로그아읏 api 연동 및 테스트")
        }
        Button(
            onClick = {
                onNavigateToLoginScreen()
            },
        ) {
            Text("자신의 정보 수정")
        }
        Button(
            onClick = {
                viewModel.onSearchMyInfo()
            },
        ) {
            Text("자신의 정보 조회")
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(onNavigateToLoginScreen = {})
}
