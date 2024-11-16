package com.kiwe.manager.ui.dashboard

import androidx.lifecycle.ViewModel
import com.kiwe.domain.exception.APIException
import com.kiwe.domain.model.MenuCategoryParam
import com.kiwe.domain.usecase.manager.menu.GetMenuSuggestedUseCase
import com.kiwe.domain.usecase.order.GetLastMonthOrderUseCase
import com.kiwe.domain.usecase.order.GetRecentSixMonthOrderUseCase
import com.kiwe.domain.usecase.order.GetTopSellingMenusSortByAgeUseCase
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
class DashBoardViewModel
    @Inject
    constructor(
        private val getTotalPriceLastMonthUseCase: GetTotalPriceLastMonthUseCase,
        private val getTotalPriceRecentSixMonthsUseCase: GetTotalPriceRecentSixMonthsUseCase,
        private val getLastMonthOrderUseCase: GetLastMonthOrderUseCase,
        private val getRecentSixMonthOrderUseCase: GetRecentSixMonthOrderUseCase,
        private val getTopSellingMenusSortByAgeUseCase: GetTopSellingMenusSortByAgeUseCase,
        private val getMenuSuggestedUseCase: GetMenuSuggestedUseCase
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
                val lastMonthSale = getTotalPriceLastMonthUseCase().getOrThrow()
                val totalSalesRecent6Month = getTotalPriceRecentSixMonthsUseCase().getOrThrow()
                val lastMonthOrder = getLastMonthOrderUseCase().getOrThrow()
                val totalOrderRecent6Month = getRecentSixMonthOrderUseCase().getOrThrow()
                val topSellingMenusSortByAge = getTopSellingMenusSortByAgeUseCase().getOrThrow().toSortedMap()
                val listMenuSuggested = getMenuSuggestedUseCase().getOrThrow()
                reduce {
                    state.copy(
                        lastMonthSale = lastMonthSale,
                        lastMonthOrder = lastMonthOrder,
                        totalSalesRecent6Month = totalSalesRecent6Month,
                        totalOrderRecent6Month = totalOrderRecent6Month,
                        topSellingMenusSortByAge = topSellingMenusSortByAge,
                        listMenuSuggested = listMenuSuggested
                    )
                }
            }
        }
    }

@Immutable
data class DashBoardState(
    val lastMonthSale: Int = 0,
    val lastMonthOrder: Int = 0,
    val totalSalesRecent6Month: Map<String, Int> = emptyMap(),
    val totalOrderRecent6Month: Map<String, Int> = emptyMap(),
    val topSellingMenusSortByAge: Map<String, Map<String, Int>> = emptyMap(),
    val listMenuSuggested: List<MenuCategoryParam> = emptyList()
)

sealed interface DashBoardSideEffect {
    class Toast(
        val message: String,
    ) : DashBoardSideEffect
}
