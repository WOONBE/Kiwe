package com.kiwe.manager.ui.login

import android.widget.Toast
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

@Composable
fun SignUpScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToLoginScreen: () -> Unit,
) {
//    val state = viewModel.collectAsState().value
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
    SignUpScreen(
        onNavigateToLoginScreen = onNavigateToLoginScreen,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignUpScreen(onNavigateToLoginScreen: () -> Unit) {
    Surface(
        modifier = Modifier.systemBarsPadding(),
    ) {
        Button(
            onClick = {
                onNavigateToLoginScreen()
            },
        ) {
            Text("SignUp Screen")
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
