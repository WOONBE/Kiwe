package com.kiwe.manager.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
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

    LazyColumn {
        item {

            Text("Home 화면")
            Button(
                onClick =
                viewModel::onLogout,
            ) {
                Text("로그아읏 api 연동 및 테스트")
            }
            Button(
                onClick = {
                    viewModel.onEditMyInfo()
                },
            ) {
                Text("자신의 정보 수정")
            }
            Button(
                onClick = {
                    viewModel.onGetLastMonthIncome()
                },
            ) {
                Text("한달간 총 주문 금액")
            }
            Button(
                onClick = {
                    viewModel.onGetOrder()
                },
            ) {
                Text("주문 단건 조회")
            }
            Button(
                onClick = {
                    viewModel.checkOrderStatus()
                },
            ) {
                Text("주문 상태 확인")
            }
            Button(
                onClick = {
                    viewModel.onGetKioskTotalOrdersLast6Months()
                },
            ) {
                Text("특정 키오스크의 6개월간 주문 금액")
            }
            Button(
                onClick = {
                    viewModel.onGetKioskTotalOrdersLastMonth()
                },
            ) {
                Text("특정 키오스크의 한달간 총 주문 금액")
            }
            Button(
                onClick = {
                    viewModel.onSearchMyInfo()
                },
            ) {
                Text("주문 전체 조회")
            }
            Button(
                onClick = {
                    viewModel.onSearchMyInfo()
                },
            ) {
                Text("키오스크 조회 API 연동 및 테스트")
            }
            Button(
                onClick = {
                    viewModel.onSearchMyInfo()
                },
            ) {
                Text("키오스크 생성 API 연동 및 테스트")
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(onNavigateToLoginScreen = {})
}
