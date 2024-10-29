package com.kiwe.manager.ui.login

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiwe.manager.R
import com.kiwe.manager.ui.component.LoginTextField
import com.kiwe.manager.ui.theme.KIWEAndroidTheme
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToSignUpScreen: () -> Unit,
    onNavigateToFindPassWordScreen: () -> Unit,
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is LoginSideEffect.Toast ->
                Toast
                    .makeText(
                        context,
                        sideEffect.message,
                        Toast.LENGTH_SHORT,
                    ).show()

            LoginSideEffect.NavigateToMainActivity -> {
//                context.startActivity(
//                    Intent(
//                        context,
//                        MainActivity::class.java,
//                    ).apply {
//                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                    },
//                )
            }
        }
    }

    LoginScreen(
        id = state.id,
        password = state.password,
        onIdChange = viewModel::onIdChange,
        onPasswordChange = viewModel::onPasswordChange,
        onNavigateToSignUpScreen = onNavigateToSignUpScreen,
        onNavigateToFindPassWordScreen = onNavigateToFindPassWordScreen,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginScreen(
    id: String,
    password: String,
    onIdChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onNavigateToSignUpScreen: () -> Unit,
    onNavigateToFindPassWordScreen: () -> Unit,
) {
    Surface(
        color = colorResource(R.color.login_dark),
        contentColor = colorResource(R.color.white),
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize(),
            ) {
                Spacer(Modifier.weight(1F))
                Column(modifier = Modifier.weight(6F)) {
                    Text(
                        modifier =
                            Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(bottom = 20.dp),
                        text = "환영합니다",
                        fontWeight = FontWeight.Bold,
                    )
                    Text(text = "Id")
                    LoginTextField(
                        value = id,
                        onValueChange = onIdChange,
                        placeHolderText = "아이디를 입력해주세요",
                    )

                    Text(text = "Password")
                    LoginTextField(
                        value = password,
                        onValueChange = onPasswordChange,
                        placeHolderText = "비밀번호를 입력해주세요",
                    )
                    Row(
                        modifier =
                            Modifier
                                .padding(top = 30.dp),
                    ) {
                        Spacer(
                            modifier = Modifier.weight(1F),
                        )
                        Button(
                            modifier =
                                Modifier
                                    .weight(2F)
                                    .padding(bottom = 10.dp),
                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor = colorResource(R.color.login_btn),
                                ),
                            shape = RoundedCornerShape(size = 12.dp),
                            content = {
                                Text(text = "로그인")
                            },
                            onClick = {
                            },
                        )
                        Spacer(
                            modifier = Modifier.weight(1F),
                        )
                    }

                    Text(
                        modifier =
                            Modifier
                                .padding(top = 20.dp)
                                .clickable {
                                    onNavigateToFindPassWordScreen()
                                },
                        text = "비밀번호를 잊어버리셨나요?",
                        color = colorResource(R.color.login_text_button),
                    )
                    Text(
                        modifier =
                            Modifier.clickable {
                                onNavigateToSignUpScreen()
                            },
                        text = "점주 등록을 진행하지 않으셨나요?",
                        color = colorResource(R.color.login_text_button),
                    )
                }
                Spacer(Modifier.weight(1F))
            }
        }
    }
}

@Preview(device = "spec: width=2304dp, height=1440dp")
@Composable
private fun LoginScreenPreview() {
    KIWEAndroidTheme {
        LoginScreen(
            onNavigateToSignUpScreen = {},
            onNavigateToFindPassWordScreen = {},
        )
    }
}
