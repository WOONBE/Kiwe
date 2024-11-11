package com.kiwe.manager.ui.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiwe.manager.ui.theme.KIWEAndroidTheme
import org.orbitmvi.orbit.compose.collectSideEffect

private const val TAG = "FindPassWordScreen 싸피"

@Composable
fun FindPassWordScreen(
    viewModel: FindPasswordViewModel = hiltViewModel(),
    onNavigateToLoginScreen: () -> Unit,
) {
//    val state = viewModel.collectAsState().value
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is FindPasswordSideEffect.Toast ->{
                Toast
                    .makeText(
                        context,
                        sideEffect.message,
                        Toast.LENGTH_SHORT,
                    ).show()

                Log.d(TAG, "FindPassWordScreen: ${sideEffect.message}")
            }


            FindPasswordSideEffect.NavigateToLoginScreen -> {}
        }
    }

    FindPassWordScreen(
        onNavigateToLoginScreen = onNavigateToLoginScreen,
        onSearchMemberByEmail = viewModel::onSearchMemberByEmail,
        onSearchMemberById = viewModel::onSearchMemberById,
        onSearchAllMember = viewModel::onSearchAllMember
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FindPassWordScreen(
    onNavigateToLoginScreen: () -> Unit,
    onSearchMemberByEmail: () -> Unit,
    onSearchMemberById: () -> Unit,
    onSearchAllMember: () -> Unit,
) {
    Surface(
        modifier = Modifier.systemBarsPadding(),
    ) {
        Column {
            Button(
                onClick = {
                    onNavigateToLoginScreen()
                },
            ) {
                Text("FindPassWordScreen")
            }
            Button(
                onClick = {
                    onSearchMemberByEmail()
                },
            ) {
                Text("이메일로 회원 조회")
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
                    onNavigateToLoginScreen()
                },
            ) {
                Text("자신의 정보 조회")
            }
            Button(
                onClick = {
                    onNavigateToLoginScreen()
                },
            ) {
                Text("회원 수정")
            }
            Button(
                onClick = {
                    onSearchMemberById()
                },
            ) {
                Text("ID로 회원 조회")
            }
            Button(
                onClick = {
                    onSearchAllMember()
                },
            ) {
                Text("모든 회원 조회")
            }
            Button(
                onClick = {
                    onNavigateToLoginScreen()
                },
            ) {
                Text("로그아웃")
            }
            Button(
                onClick = {
                    onNavigateToLoginScreen()
                },
            ) {
                Text("메뉴 카테고리별 조회")
            }
            Button(
                onClick = {
                    onNavigateToLoginScreen()
                },
            ) {
                Text("메뉴 삭제")
            }
            Button(
                onClick = {
                    onNavigateToLoginScreen()
                },
            ) {
                Text("메뉴 생성")
            }
            Button(
                onClick = {
                    onNavigateToLoginScreen()
                },
            ) {
                Text("메뉴 전체 조회")
            }
            Button(
                onClick = {
                    onNavigateToLoginScreen()
                },
            ) {
                Text("메뉴 수정")
            }
            Button(
                onClick = {
                    onNavigateToLoginScreen()
                },
            ) {
                Text("메뉴 단건 조회")
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    KIWEAndroidTheme {
        FindPassWordScreen(
            onNavigateToLoginScreen = {},
        )
    }
}
