package com.kiwe.manager.ui.login

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.ViewModel
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
                                postSideEffect(SignUpSideEffect.Toast(message = throwable.message.orEmpty()))
                            }
                        }
                },
            )

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
    val passwordVisualTransformation: VisualTransformation = VisualTransformation.None,
    val passwordRepeatVisualTransformation: VisualTransformation = VisualTransformation.None,
    val showPasswordRepeat: Boolean = false,
)

sealed interface SignUpSideEffect {
    class Toast(
        val message: String,
    ) : SignUpSideEffect
}
