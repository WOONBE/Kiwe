package com.kiwe.manager.ui.login

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.ViewModel
import com.kiwe.domain.exception.APIException
import com.kiwe.domain.model.SignUpParam
import com.kiwe.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
    @Inject
    constructor(
        private val signUpUseCase: SignUpUseCase,
//        private val loginUseCase: LoginUseCase,
//        private val setTokenUseCase: SetTokenUseCase,
    ) : ViewModel(),
        ContainerHost<SignUpState, SignUpSideEffect> {
        override val container: Container<SignUpState, SignUpSideEffect> =
            container(
                initialState = SignUpState(),
                buildSettings = {
                    this.exceptionHandler =
                        CoroutineExceptionHandler { _, throwable ->
                            intent {
                                if (throwable is APIException) {
                                    postSideEffect(
                                        SignUpSideEffect.Toast(
                                            "${throwable.code} : " +
                                                (throwable.message ?: "알수 없는 에러"),
                                        ),
                                    )
                                } else {
                                    postSideEffect(SignUpSideEffect.Toast(throwable.message ?: "알수 없는 에러"))
                                }
                            }
                        }
                },
            )

        fun onSignUp() =
            intent {
                if (state.password != state.passwordRepeat) {
                    postSideEffect(SignUpSideEffect.Toast(message = "패스워드를 다시 확인해주세요."))
                    return@intent
                }

                signUpUseCase(
                    SignUpParam(
                        name = state.name,
                        password = state.password,
                        email = state.id,
                        kioskIds = listOf(),
                    ),
                ).getOrThrow()

                postSideEffect(SignUpSideEffect.NavigateToLoginScreen)
                postSideEffect(SignUpSideEffect.Toast(message = "회원가입에 성공했습니다"))
            }

        fun onNameChange(name: String) =
            blockingIntent {
                reduce {
                    state.copy(name = name)
                }
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

        fun onPasswordRepeatChange(repeatPassword: String) =
            blockingIntent {
                reduce {
                    state.copy(passwordRepeat = repeatPassword)
                }
            }

        fun onShowPasswordChange() =
            intent {
                reduce {
                    if (!state.showPassword) {
                        state.copy(
                            showPassword = true,
                            passwordImageVector = Icons.Filled.Visibility,
                            passwordVisualTransformation = VisualTransformation.None,
                        )
                    } else {
                        state.copy(
                            showPassword = false,
                            passwordImageVector = Icons.Filled.VisibilityOff,
                            passwordVisualTransformation = PasswordVisualTransformation(),
                        )
                    }
                }
            }

        fun onShowPasswordRepeatChange() =
            intent {
                reduce {
                    if (!state.showPasswordRepeat) {
                        state.copy(
                            showPasswordRepeat = true,
                            passwordRepeatImageVector = Icons.Filled.Visibility,
                            passwordRepeatVisualTransformation = VisualTransformation.None,
                        )
                    } else {
                        state.copy(
                            showPasswordRepeat = false,
                            passwordRepeatImageVector = Icons.Filled.VisibilityOff,
                            passwordRepeatVisualTransformation = PasswordVisualTransformation(),
                        )
                    }
                }
            }
    }

@Immutable
data class SignUpState(
    val name: String = "",
    val id: String = "",
    val password: String = "",
    val passwordRepeat: String = "",
    val showPassword: Boolean = false,
    val passwordImageVector: ImageVector = Icons.Filled.VisibilityOff,
    val passwordRepeatImageVector: ImageVector = Icons.Filled.VisibilityOff,
    val passwordVisualTransformation: VisualTransformation = PasswordVisualTransformation(),
    val passwordRepeatVisualTransformation: VisualTransformation = PasswordVisualTransformation(),
    val showPasswordRepeat: Boolean = false,
)

sealed interface SignUpSideEffect {
    object NavigateToLoginScreen : SignUpSideEffect

    class Toast(
        val message: String,
    ) : SignUpSideEffect
}
