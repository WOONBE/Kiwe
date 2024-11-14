package com.kiwe.manager.ui.home

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.kiwe.manager.ui.component.HomeSideBar
import com.kiwe.manager.ui.theme.KIWEAndroidTheme
import dagger.hilt.android.AndroidEntryPoint
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    private val homeViewModel: HomeViewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val state = homeViewModel.collectAsState().value
            val navController = rememberNavController()

            LaunchedEffect(state.tabIdx) {
                when (state.tabIdx) {
                    0 -> {
                        navController.navigate(HomeRoute.DashBoardScreen.route){
                            popUpTo(HomeRoute.DashBoardScreen.route){
                                inclusive = true
                            }
                        }
                    }
                    1 -> {
                        navController.navigate(HomeRoute.MenuManagementScreen.route){
                            popUpTo(HomeRoute.MenuManagementScreen.route){
                                inclusive = true
                            }
                        }
                    }
                    2 -> {
                        navController.navigate(HomeRoute.SalesOverviewScreen.route){
                            popUpTo(HomeRoute.SalesOverviewScreen.route){
                                inclusive = true
                            }
                        }
                    }
                    3 -> {
                        navController.navigate(HomeRoute.KioskManagementScreen.route){
                            popUpTo(HomeRoute.KioskManagementScreen.route){
                                inclusive = true
                            }
                        }
                    }
                    4 -> {
                        navController.navigate(HomeRoute.SettingScreen.route){
                            popUpTo(HomeRoute.SettingScreen.route){
                                inclusive = true
                            }
                        }
                    }
                }
            }
            KIWEAndroidTheme {
                Surface(modifier = Modifier.systemBarsPadding()) {
                    homeViewModel.collectSideEffect { sideEffect ->
                        when (sideEffect) {
                            is HomeSideEffect.Toast ->
                                Toast
                                    .makeText(
                                        this,
                                        sideEffect.message,
                                        Toast.LENGTH_SHORT,
                                    ).show()

                            HomeSideEffect.NavigateToDashBoardScreen -> {
                            }
                            else -> {}
                        }
                    }
                    Row {
                        HomeSideBar(
                            tabIdx = state.tabIdx,
                            onTabChanged = { idx -> homeViewModel.onTabChanged(idx) },
                        )
                        HomeNavHost(navController)
                    }
                }
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
            else -> {}
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
                    viewModel.onGetOrderAll()
                },
            ) {
                Text("주문 전체 조회")
            }
            Button(
                onClick = {
                    viewModel.onGetKioskByKioskId()
                },
            ) {
                Text("키오스크 조회 API 연동 및 테스트")
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(onNavigateToLoginScreen = {})
}
