package com.kiwe.kiosk.login

import androidx.lifecycle.ViewModel
import com.kiwe.domain.exception.APIException
import com.kiwe.domain.model.CreateKioskRequest
import com.kiwe.domain.model.LoginParam
import com.kiwe.domain.model.Token
import com.kiwe.domain.usecase.manager.kiosk.CreateKioskUseCase
import com.kiwe.domain.usecase.manager.login.LoginUseCase
import com.kiwe.domain.usecase.manager.search.SearchMyInfoUseCase
import com.kiwe.domain.usecase.manager.token.SetTokenUseCase
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
        private val loginUseCase: LoginUseCase,
        private val setTokenUseCase: SetTokenUseCase,
        private val searchMyInfoUseCase: SearchMyInfoUseCase,
        private val createKioskUseCase: CreateKioskUseCase,
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
                                if (throwable is APIException) {
                                    postSideEffect(
                                        LoginSideEffect.Toast(
                                            "${throwable.code} : " +
                                                (throwable.message ?: "알수 없는 에러"),
                                        ),
                                    )
                                } else {
                                    postSideEffect(
                                        LoginSideEffect.Toast(
                                            throwable.message ?: "알수 없는 에러",
                                        ),
                                    )
                                }
                            }
                        }
                },
            )

        fun onLoginClick() =
            intent {
                if (state.id.isEmpty() || state.password.isEmpty()) {
                    postSideEffect(LoginSideEffect.Toast("이메일과 비밀번호를 입력해주세요!"))
                    return@intent
                }
                val response =
                    loginUseCase(
                        LoginParam(
                            email = state.id,
                            password = state.password,
                        ),
                    ).getOrThrow()
                setTokenUseCase(
                    Token(
                        response.accessToken,
                        response.refreshToken,
                    ),
                )
                val userInfo = searchMyInfoUseCase().getOrThrow()
                val ownerId = userInfo.id
                val requestCreateKiosk =
                    CreateKioskRequest(
                        location = "SSAFY",
                        status = "READY",
                        ownerId = ownerId,
                    )
                val createKioskResponse =
                    createKioskUseCase(requestCreateKiosk).getOrThrow()

                postSideEffect(LoginSideEffect.Toast("환영합니다!"))
                postSideEffect(LoginSideEffect.NavigateToHomeActivity)
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

    object NavigateToHomeActivity : LoginSideEffect
}
