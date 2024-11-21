package com.kiwe.kiosk.ui.screen.order

import com.kiwe.domain.exception.APIException
import com.kiwe.domain.model.MenuCategory
import com.kiwe.domain.model.MenuCategoryGroup
import com.kiwe.domain.model.MenuCategoryParam
import com.kiwe.domain.usecase.GetCategoryListUseCase
import com.kiwe.kiosk.base.BaseSideEffect
import com.kiwe.kiosk.base.BaseState
import com.kiwe.kiosk.base.BaseViewModel
import com.kiwe.kiosk.model.OrderItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class OrderViewModel
    @Inject
    constructor(
        private val getCategoryListUseCase: GetCategoryListUseCase,
    ) : BaseViewModel<OrderState, OrderSideEffect>(OrderState()) {
        override fun handleExceptionIntent(
            coroutineContext: CoroutineContext,
            throwable: Throwable,
        ) {
            intent {
                if (throwable is APIException) {
                    postSideEffect(
                        OrderSideEffect.Toast(
                            "${throwable.code} : " +
                                (throwable.message ?: "알수 없는 에러"),
                        ),
                    )
                } else {
                    postSideEffect(OrderSideEffect.Toast(throwable.message ?: "알수 없는 에러"))
                }
            }
        }

        init {
            intent {
                setCategory(state.selectedCategoryGroup)
            }
        }

        fun setCategory(selectGroup: MenuCategoryGroup) =
            intent {
                // 선택된 그룹에 속하는 모든 카테고리를 가져옴
                val categories = MenuCategory.entries.filter { it.group == selectGroup }

                // 각 카테고리에 대해 getCategoryListUseCase 호출을 flow로 만듦
                val menuFlows =
                    categories.map { category ->
                        flowOf(getCategoryListUseCase(category.displayName).getOrThrow())
                    }

                // combine을 사용하여 모든 카테고리의 메뉴 목록을 결합
                combine(menuFlows) { menusArray ->
                    menusArray.flatMap { it } // 각 카테고리별 메뉴 리스트를 한 리스트로 합침
                }.collect { combinedMenuList ->
                    reduce {
                        state.copy(
                            selectedCategoryGroup = selectGroup, // 선택한 그룹을 상태에 저장
                            menuList = combinedMenuList.chunked(12), // 합쳐진 메뉴 리스트를 12개씩 분할
                        )
                    }
                }
            }
    }

data class OrderState(
    val selectedCategoryGroup: MenuCategoryGroup = MenuCategoryGroup.NEW,
    val menuList: List<List<MenuCategoryParam>> = listOf(),
    val orderItem: List<OrderItem> =
        listOf(
            OrderItem(
                menuTitle = "디카페인 카페모카",
                menuPrice = 4500,
                menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
            ),
            OrderItem(
                menuTitle = "디카페인 카페모카",
                menuPrice = 4500,
                menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
            ),
            OrderItem(
                menuTitle = "디카페인 카페모카",
                menuPrice = 4500,
                menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
            ),
            OrderItem(
                menuTitle = "디카페인 카페모카",
                menuPrice = 4500,
                menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
            ),
            OrderItem(
                menuTitle = "디카페인 카페모카",
                menuPrice = 4500,
                menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
            ),
            OrderItem(
                menuTitle = "디카페인 카페모카",
                menuPrice = 4500,
                menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
            ),
            OrderItem(
                menuTitle = "디카페인 카페모카",
                menuPrice = 4500,
                menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
            ),
            OrderItem(
                menuTitle = "디카페인 카페모카",
                menuPrice = 4500,
                menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
            ),
        ),
) : BaseState

sealed interface OrderSideEffect : BaseSideEffect {
    data class Toast(
        val message: String,
    ) : OrderSideEffect

    data object NavigateToNextScreen : OrderSideEffect
}
