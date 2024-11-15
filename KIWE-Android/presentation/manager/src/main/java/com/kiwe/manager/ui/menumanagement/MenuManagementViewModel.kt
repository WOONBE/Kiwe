package com.kiwe.manager.ui.menumanagement

import androidx.lifecycle.ViewModel
import com.kiwe.domain.exception.APIException
import com.kiwe.domain.model.MenuCategoryParam
import com.kiwe.domain.usecase.GetCategoryListUseCase
import com.kiwe.domain.usecase.manager.menu.DeleteMenuUseCase
import com.kiwe.domain.usecase.manager.menu.GetAllMenuListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

@HiltViewModel
class MenuManagementViewModel
    @Inject
    constructor(
        private val getAllMenuListUseCase: GetAllMenuListUseCase,
        private val getCategoryListUseCase: GetCategoryListUseCase,
        private val deleteMenuUseCase: DeleteMenuUseCase,
    ) : ViewModel(),
        ContainerHost<MenuManagementState, MenuManagementSideEffect> {
        override val container: Container<MenuManagementState, MenuManagementSideEffect> =
            container(
                initialState = MenuManagementState(),
                buildSettings = {
                    this.exceptionHandler =
                        CoroutineExceptionHandler { _, throwable ->
                            intent {
                                if (throwable is APIException) {
                                    postSideEffect(
                                        MenuManagementSideEffect.Toast(
                                            "${throwable.code} : " +
                                                (throwable.message ?: "알수 없는 에러"),
                                        ),
                                    )
                                } else {
                                    postSideEffect(
                                        MenuManagementSideEffect.Toast(
                                            throwable.message ?: "알수 없는 에러",
                                        ),
                                    )
                                }
                            }
                        }
                },
            )

        fun onChangeCategory(category: String) =
            intent {
                val categoryResponse = getCategoryListUseCase(category).getOrThrow()
                reduce {
                    state.copy(
                        category = category,
                        menuList = categoryResponse,
                    )
                }
            }

        fun onDelete(menuId: Int) =
            intent {
                deleteMenuUseCase(menuId).getOrThrow()
                val menuListResponse = getAllMenuListUseCase().getOrThrow()
                reduce {
                    state.copy(menuList = menuListResponse)
                }
            }

        init {
            intent {
                val menuListResponse = getAllMenuListUseCase().getOrThrow()
                reduce {
                    state.copy(menuList = menuListResponse)
                }
            }
        }
    }

@Immutable
data class MenuManagementState(
    val menuList: List<MenuCategoryParam> = emptyList(),
    val category: String = "전체",
)

sealed interface MenuManagementSideEffect {
    object NavigateToLoginScreen : MenuManagementSideEffect

    class Toast(
        val message: String,
    ) : MenuManagementSideEffect
}
