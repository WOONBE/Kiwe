package com.kiwe.manager.ui.login

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.ViewModel
import com.kiwe.domain.exception.APIException
import com.kiwe.domain.usecase.manager.search.SearchMemberByEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

@HiltViewModel
class FindPasswordViewModel
    @Inject
    constructor(
        private val searchMemberByEmailUseCase: SearchMemberByEmailUseCase,
    ) : ViewModel(),
        ContainerHost<FindPasswordState, FindPasswordSideEffect> {
        override val container: Container<FindPasswordState, FindPasswordSideEffect> =
            container(
                initialState = FindPasswordState(),
                buildSettings = {
                    this.exceptionHandler =
                        CoroutineExceptionHandler { _, throwable ->
                            intent {
                                if (throwable is APIException) {
                                    postSideEffect(
                                        FindPasswordSideEffect.Toast(
                                            "${throwable.code} : " +
                                                (throwable.message ?: "알수 없는 에러"),
                                        ),
                                    )
                                } else {
                                    postSideEffect(
                                        FindPasswordSideEffect.Toast(
                                            throwable.message ?: "알수 없는 에러",
                                        ),
                                    )
                                }
                            }
                        }
                },
            )

        fun onSearchMemberByEmailUseCase() =
            intent {
                val response = searchMemberByEmailUseCase(state.searchMemberByEmail).getOrThrow()
                postSideEffect(FindPasswordSideEffect.Toast(response.toString()))
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
data class FindPasswordState(
    val searchMemberByEmail: String = "2",
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

sealed interface FindPasswordSideEffect {
    object NavigateToLoginScreen : FindPasswordSideEffect

    class Toast(
        val message: String,
    ) : FindPasswordSideEffect
}
