package com.kiwe.manager.ui.login

sealed class LoginRoute(
    val name: String,
) {
    object LoginScreen : LoginRoute("LoginScreen")

    object SignUpScreen : LoginRoute("SignUpScreen")

    object FindPassWordScreen : LoginRoute("FindPassWordScreen")
}
