package com.kiwe.manager.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.kiwe.domain.exception.APIException
import com.kiwe.domain.model.EditMemberParam
import com.kiwe.domain.model.LogoutParam
import com.kiwe.domain.usecase.manager.edit.EditMyInfoUseCase
import com.kiwe.domain.usecase.manager.login.ClearTokenUseCase
import com.kiwe.domain.usecase.manager.login.LogoutUseCase
import com.kiwe.domain.usecase.manager.search.SearchMyInfoUseCase
import com.kiwe.domain.usecase.manager.token.GetTokenUseCase
import com.kiwe.domain.usecase.order.CheckOrderStatusUseCase
import com.kiwe.domain.usecase.order.GetKioskTotalOrdersLast6MonthsUseCase
import com.kiwe.domain.usecase.order.GetLastMonthIncomeUseCase
import com.kiwe.domain.usecase.order.GetOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

private const val TAG = "HomeViewModel 싸피"

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val getTokenUseCase: GetTokenUseCase,
        private val logoutUseCase: LogoutUseCase,
        private val clearTokenUseCase: ClearTokenUseCase,
        private val searchMyInfoUseCase: SearchMyInfoUseCase,
        private val editMyInfoUseCase: EditMyInfoUseCase,
        private val getLastMonthIncomeUseCase: GetLastMonthIncomeUseCase,
        private val getOrderUseCase: GetOrderUseCase,
        private val checkOrderStatusUseCase: CheckOrderStatusUseCase,
        private val getKioskTotalOrdersLast6MonthsUseCase: GetKioskTotalOrdersLast6MonthsUseCase,
    ) : ViewModel(),
        ContainerHost<HomeState, HomeSideEffect> {
        override val container: Container<HomeState, HomeSideEffect> =
            container(
                initialState = HomeState(),
                buildSettings = {
                    this.exceptionHandler =
                        CoroutineExceptionHandler { _, throwable ->
                            intent {
                                if (throwable is APIException) {
                                    postSideEffect(
                                        HomeSideEffect.Toast(
                                            "${throwable.code} : " +
                                                (throwable.message ?: "알수 없는 에러"),
                                        ),
                                    )
                                } else {
                                    postSideEffect(
                                        HomeSideEffect.Toast(
                                            throwable.message ?: "알수 없는 에러",
                                        ),
                                    )
                                }
                            }
                        }
                },
            )

        fun onLogout() =
            intent {
                val token = getTokenUseCase()

                if (token != null) {
                    logoutUseCase(
                        LogoutParam(
                            token.refreshToken,
                        ),
                    ).getOrThrow()
                    Log.d(TAG, "onLogout: 여기까지는 되나?")
                    clearTokenUseCase()
                    Log.d(TAG, "onLogout: clearToken도 되나?")
                    postSideEffect(HomeSideEffect.Toast(message = "로그아웃 되었습니다!"))
                    postSideEffect(HomeSideEffect.NavigateToLoginScreen)
                } else {
                    postSideEffect(HomeSideEffect.Toast(message = "정상적인 로그아웃에 실패했습니다!"))
                }
            }

        fun onSearchMyInfo() =
            intent {
                val response = searchMyInfoUseCase().getOrThrow()
                postSideEffect(HomeSideEffect.Toast(response.toString()))
            }

        fun onEditMyInfo() =
            intent {
                val response =
                    editMyInfoUseCase(
                        editMemberParam =
                            EditMemberParam(
                                name = state.name,
                                email = state.email,
                                password = state.password,
                                kioskIds = state.kioskIds,
                            ),
                    ).getOrThrow()
                postSideEffect(HomeSideEffect.Toast(response.toString()))
            }

        fun onGetLastMonthIncome() =
            intent {
                val response = getLastMonthIncomeUseCase().getOrThrow()
                postSideEffect(HomeSideEffect.Toast(response.toString()))
            }

        fun onGetOrder() =
            intent {
                val response = getOrderUseCase(state.orderId).getOrThrow()
                postSideEffect(HomeSideEffect.Toast(response.toString()))
            }

        fun checkOrderStatus() =
            intent {
                val response = checkOrderStatusUseCase(state.kioskId).getOrThrow()
                postSideEffect(HomeSideEffect.Toast(response))
            }

        fun onGetKioskTotalOrdersLast6Months() =
            intent {
                val response = getKioskTotalOrdersLast6MonthsUseCase(state.kioskId).getOrThrow()
                postSideEffect(HomeSideEffect.Toast(response.toString()))
            }
    }

@Immutable
data class HomeState(
    val name: String = "5",
    val id: String = "5",
    val password: String = "5",
    val email: String = "5",
    val orderId: Int = 170,
    val kioskId: Int = 8,
    val kioskIds: List<Int> = listOf(),
)

sealed interface HomeSideEffect {
    object NavigateToLoginScreen : HomeSideEffect

    class Toast(
        val message: String,
    ) : HomeSideEffect
}
