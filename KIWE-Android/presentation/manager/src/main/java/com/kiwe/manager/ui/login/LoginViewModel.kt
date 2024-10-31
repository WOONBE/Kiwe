package com.kiwe.manager.ui.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
//        private val loginUseCase: LoginUseCase,
//        private val setTokenUseCase: SetTokenUseCase,
    ) : ViewModel(),
        ContainerHost<LoginState, LoginSideEffect> {
        override val container: Container<LoginState, LoginSideEffect> =
            container(
                initialState = LoginState(),
                buildSettings = {
                    this.exceptionHandler =
                        CoroutineExceptionHandler { _, throwable ->
                            intent {
                                postSideEffect(LoginSideEffect.Toast(message = throwable.message.orEmpty()))
                            }
                        }
                },
            )

        fun onLoginClick() =
            intent {
//                val id = state.id
//                val password = state.password
//                val token = loginUseCase(id, password).getOrThrow()
//                setTokenUseCase(token)
//        postSideEffect(LoginSideEffect.Toast(message = "token = $token"))
                postSideEffect(LoginSideEffect.NavigateToMainActivity)
            }

        fun onIdChange(id: String) =
            blockingIntent {
                reduce {
                    state.copy(id = id)
                }
            }

        fun onPasswordChange(password: String) =
            blockingIntent {
                reduce {
                    state.copy(password = password)
                }
            }
    }

@Immutable
data class LoginState(
    val id: String = "",
    val password: String = "",
)

sealed interface LoginSideEffect {
    class Toast(
        val message: String,
    ) : LoginSideEffect

    object NavigateToMainActivity : LoginSideEffect
}