package com.kiwe.manager.ui.login

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiwe.manager.R
import com.kiwe.manager.ui.component.ContentArea
import com.kiwe.manager.ui.component.LoginTextFieldForm
import com.kiwe.manager.ui.theme.KIWEAndroidTheme
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    onNavigateToLoginScreen: () -> Unit,
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is SignUpSideEffect.Toast ->
                Toast
                    .makeText(
                        context,
                        sideEffect.message,
                        Toast.LENGTH_SHORT,
                    ).show()

            SignUpSideEffect.NavigateToLoginScreen -> onNavigateToLoginScreen()
        }
    }

    SignUpScreen(
        name = state.name,
        id = state.id,
        password = state.password,
        passwordRepeat = state.passwordRepeat,
        passwordImageVector = state.passwordImageVector,
        passwordVisualTransformation = state.passwordVisualTransformation,
        passwordRepeatImageVector = state.passwordRepeatImageVector,
        passwordRepeatVisualTransformation = state.passwordRepeatVisualTransformation,
        onNameChange = viewModel::onNameChange,
        onIdChange = viewModel::onIdChange,
        onPasswordChange = viewModel::onPasswordChange,
        onPasswordRepeatChange = viewModel::onPasswordRepeatChange,
        onShowPasswordChange = viewModel::onShowPasswordChange,
        onShowPasswordRepeatChange = viewModel::onShowPasswordRepeatChange,
        onSignUp = viewModel::onSignUp,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignUpScreen(
    name: String,
    id: String,
    password: String,
    passwordRepeat: String,
    passwordImageVector: ImageVector,
    passwordVisualTransformation: VisualTransformation,
    passwordRepeatImageVector: ImageVector,
    passwordRepeatVisualTransformation: VisualTransformation,
    onNameChange: (String) -> Unit,
    onIdChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordRepeatChange: (String) -> Unit,
    onShowPasswordChange: () -> Unit,
    onShowPasswordRepeatChange: () -> Unit,
    onSignUp: () -> Unit,
) {
    Surface(
        color = colorResource(R.color.login_dark),
        contentColor = colorResource(R.color.white),
    ) {
        ContentArea(rowModifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.weight(6F),
            ) {
                Text(
                    text = "환영합니다",
                    fontWeight = FontWeight.Bold,
                    modifier =
                        Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 20.dp),
                )

                LoginTextFieldForm(
                    title = "이름",
                    value = name,
                    placeHolderText = "이름을 입력해주세요",
                    onValueChange = onNameChange,
                )

                Spacer(modifier = Modifier.size(20.dp))

                LoginTextFieldForm(
                    title = "아이디",
                    value = id,
                    placeHolderText = "Id를 입력해주세요",
                    onValueChange = onIdChange,
                )
                Spacer(modifier = Modifier.size(20.dp))

                LoginTextFieldForm(
                    title = "비밀번호",
                    value = password,
                    placeHolderText = "비밀번호를 입력해주세요",
                    onValueChange = onPasswordChange,
                    imageVector = passwordImageVector,
                    onIconClick = onShowPasswordChange,
                    visualTransformation = passwordVisualTransformation,
                )
                Spacer(modifier = Modifier.size(20.dp))

                LoginTextFieldForm(
                    title = "비밀번호 재입력",
                    value = passwordRepeat,
                    placeHolderText = "비밀번호를 입력해주세요",
                    onValueChange = onPasswordRepeatChange,
                    imageVector = passwordRepeatImageVector,
                    onIconClick = onShowPasswordRepeatChange,
                    visualTransformation = passwordRepeatVisualTransformation,
                )

                ContentArea(
                    rowModifier =
                        Modifier
                            .padding(top = 30.dp),
                ) {
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
                            Text(text = "회원 가입")
                        },
                        onClick = onSignUp,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    KIWEAndroidTheme {
        SignUpScreen(
            onNavigateToLoginScreen = {},
        )
    }
}
