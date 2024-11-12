package com.kiwe.kiosk.login

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kiwe.kiosk.R
import com.kiwe.kiosk.main.MainActivity
import com.kiwe.kiosk.ui.theme.KIWEAndroidTheme
import com.kiwe.kiosk.ui.theme.KiweBlack1
import com.kiwe.kiosk.ui.theme.KiweOrange1
import com.kiwe.kiosk.ui.theme.KiweWhite1
import com.kiwe.kiosk.ui.theme.Typography
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel()) {
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

            LoginSideEffect.NavigateToHomeActivity -> {
                context.startActivity(
                    Intent(
                        context,
                        MainActivity::class.java,
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    },
                )
            }
        }
    }
    LoginScreen(
        id = state.id,
        password = state.password,
        onLoginClick = viewModel::onLoginClick,
        onIdChange = viewModel::onIdChange,
        onPasswordChange = viewModel::onPasswordChange,
    )
}

@Composable
private fun LoginScreen(
    id: String,
    password: String,
    onLoginClick: () -> Unit,
    onIdChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
) {
    Surface(
        color = KiweBlack1,
        contentColor = KiweWhite1,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // 로고 이미지
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_playstore_nobg), // 로고 이미지 리소스 추가 필요
                contentDescription = "Kiosk Logo",
                modifier =
                    Modifier
                        .size(200.dp)
                        .padding(bottom = 24.dp),
            )

            // 환영 인사 텍스트
            Text(
                text = "키오스크 등록",
                style = Typography.headlineLarge,
                modifier = Modifier.padding(bottom = 16.dp),
            )
            // ID 입력 필드 (기본 TextField 사용)
            TextField(
                value = id,
                onValueChange = onIdChange,
                label = { Text("ID") },
                placeholder = { Text("아이디를 입력해주세요") },
                colors =
                    TextFieldDefaults.colors(
                        focusedContainerColor = KiweWhite1,
                        unfocusedContainerColor = KiweWhite1,
                        disabledContainerColor = KiweWhite1,
                        focusedIndicatorColor = KiweOrange1,
                        focusedLabelColor = KiweOrange1,
                    ),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                singleLine = true,
            )

            // 비밀번호 입력 필드 (기본 TextField 사용)
            TextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text("Password") },
                placeholder = { Text("비밀번호를 입력해주세요") },
                colors =
                    TextFieldDefaults.colors(
                        focusedContainerColor = KiweWhite1,
                        unfocusedContainerColor = KiweWhite1,
                        disabledContainerColor = KiweWhite1,
                        focusedIndicatorColor = KiweOrange1,
                        focusedLabelColor = KiweOrange1,
                    ),
                visualTransformation = PasswordVisualTransformation(),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                singleLine = true,
            )

            // 로그인 버튼
            Button(
                onClick = {
                    if (id.isNotBlank() && password.isNotBlank()) {
                        onLoginClick()
                    } else {
                        // TODO
                    }
                },
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = KiweOrange1,
                    ),
                shape = RoundedCornerShape(size = 12.dp),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 16.dp),
            ) {
                Text(text = "등록하기", style = Typography.headlineLarge.copy(fontSize = 20.sp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KioskLoginScreenPreview() {
    KIWEAndroidTheme {
        LoginScreen()
    }
}
