package com.kiwe.manager.ui.dashboard

import androidx.lifecycle.ViewModel
import com.kiwe.domain.exception.APIException
import com.kiwe.domain.usecase.order.GetLastMonthIncomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel
    @Inject
    constructor(
        private val getLastMonthIncomeUseCase: GetLastMonthIncomeUseCase,
    ) : ViewModel(),
        ContainerHost<DashBoardState, DashBoardSideEffect> {
        override val container: Container<DashBoardState, DashBoardSideEffect> =
            container(
                initialState = DashBoardState(),
                buildSettings = {
                    this.exceptionHandler =
                        CoroutineExceptionHandler { _, throwable ->
                            intent {
                                if (throwable is APIException) {
                                    postSideEffect(
                                        DashBoardSideEffect.Toast(
                                            "${throwable.code} : " +
                                                (throwable.message ?: "알수 없는 에러"),
                                        ),
                                    )
                                } else {
                                    postSideEffect(
                                        DashBoardSideEffect.Toast(
                                            throwable.message ?: "알수 없는 에러",
                                        ),
                                    )
                                }
                            }
                        }
                },
            )

        init {
            intent {
                val income = getLastMonthIncomeUseCase().getOrThrow()
                reduce {
                    state.copy(
                        lastMonthIncome = income,
                    )
                }
            }
        }
    }

@Immutable
data class DashBoardState(
    val lastMonthIncome: Int = 0,
)

sealed interface DashBoardSideEffect {
    class Toast(
        val message: String,
    ) : DashBoardSideEffect
}
