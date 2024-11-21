package com.kiwe.manager.ui.salesoverview

import androidx.lifecycle.ViewModel
import com.kiwe.domain.exception.APIException
import com.kiwe.domain.model.MenuCategoryParam
import com.kiwe.domain.usecase.order.GetLastMonthOrderUseCase
import com.kiwe.domain.usecase.order.GetRecentSixMonthOrderUseCase
import com.kiwe.domain.usecase.order.GetTotalPriceLastMonthUseCase
import com.kiwe.domain.usecase.order.GetTotalPriceRecentSixMonthsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

@HiltViewModel
class SalesOverViewViewModel
    @Inject
    constructor(
        private val getTotalPriceLastMonthUseCase: GetTotalPriceLastMonthUseCase,
        private val getTotalPriceRecentSixMonthsUseCase: GetTotalPriceRecentSixMonthsUseCase,
        private val getLastMonthOrderUseCase: GetLastMonthOrderUseCase,
        private val getRecentSixMonthOrderUseCase: GetRecentSixMonthOrderUseCase,
    ) : ViewModel(),
        ContainerHost<SalesOverViewState, SalesOverViewSideEffect> {
        override val container: Container<SalesOverViewState, SalesOverViewSideEffect> =
            container(
                initialState = SalesOverViewState(),
                buildSettings = {
                    this.exceptionHandler =
                        CoroutineExceptionHandler { _, throwable ->
                            intent {
                                if (throwable is APIException) {
                                    postSideEffect(
                                        SalesOverViewSideEffect.Toast(
                                            "${throwable.code} : " +
                                                (throwable.message ?: "알수 없는 에러"),
                                        ),
                                    )
                                } else {
                                    postSideEffect(
                                        SalesOverViewSideEffect.Toast(
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
                val totalPriceLastMonth = getTotalPriceLastMonthUseCase().getOrThrow()
                val totalPriceRecent6Month = getTotalPriceRecentSixMonthsUseCase().getOrThrow()
                val totalOrderLastMonth = getLastMonthOrderUseCase().getOrThrow()
                val totalOrderRecent6Month = getRecentSixMonthOrderUseCase().getOrThrow()
                reduce {
                    state.copy(
                        totalPriceLastMonth = totalPriceLastMonth,
                        totalPriceRecent6Month = totalPriceRecent6Month,
                        totalOrderRecent6Month = totalOrderRecent6Month,
                        totalOrderLastMonth = totalOrderLastMonth,
                    )
                }
            }
        }
    }

@Immutable
data class SalesOverViewState(
    val totalPriceRecent6Month: Map<String, Int> = emptyMap(),
    val totalOrderRecent6Month: Map<String, Int> = emptyMap(),
    val totalPriceLastMonth: Int = 0,
    val totalOrderLastMonth: Int = 0,
    val category: String = "전체",
    val itemEdited: MenuCategoryParam =
        MenuCategoryParam(
            id = 9694,
            category = "lacus",
            categoryNumber = 5465,
            hotOrIce = "definitiones",
            name = "Clair Bryant",
            price = 4876,
            description = "putent",
            imgPath = "viderer",
        ),
)

sealed interface SalesOverViewSideEffect {
    object NavigateToLoginScreen : SalesOverViewSideEffect

    class Toast(
        val message: String,
    ) : SalesOverViewSideEffect
}
