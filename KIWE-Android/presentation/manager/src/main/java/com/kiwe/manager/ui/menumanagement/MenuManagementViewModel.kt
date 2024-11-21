package com.kiwe.manager.ui.menumanagement

import androidx.lifecycle.ViewModel
import com.kiwe.domain.exception.APIException
import com.kiwe.domain.model.MenuCategoryParam
import com.kiwe.domain.model.MenuParam
import com.kiwe.domain.usecase.GetCategoryListUseCase
import com.kiwe.domain.usecase.manager.menu.CreateMenuUseCase
import com.kiwe.domain.usecase.manager.menu.DeleteMenuUseCase
import com.kiwe.domain.usecase.manager.menu.EditMenuUseCase
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
        private val editMenuUseCase: EditMenuUseCase,
        private val createMenuUseCase: CreateMenuUseCase,
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

        fun onClickEditItem(menuCategoryParam: MenuCategoryParam) =
            intent {
                reduce {
                    state.copy(itemEdited = menuCategoryParam)
                }
            }

        fun onEditItem(menuParam: MenuParam) =
            intent {
                val response = editMenuUseCase(state.itemEdited.id, menuParam).getOrThrow()
                postSideEffect(MenuManagementSideEffect.Toast("성공적으로 메뉴를 수정했습니다!"))
                val newList =
                    state.menuList.map {
                        if (it.id == state.itemEdited.id) {
                            response
                        } else {
                            it
                        }
                    }
                reduce {
                    state.copy(menuList = newList)
                }
            }

        fun onDelete(menuId: Int) =
            intent {
                deleteMenuUseCase(menuId).getOrThrow()
                val menuListResponse = getCategoryListUseCase(state.category).getOrThrow()
                reduce {
                    state.copy(menuList = menuListResponse)
                }
            }

        fun onCreate() =
            intent {
                createMenuUseCase(state.itemCreated).getOrThrow()
                val menuListResponse = getAllMenuListUseCase().getOrThrow()
                reduce {
                    state.copy(menuList = menuListResponse)
                }
            }

        fun onEditNewItemCategory(category: String) =
            intent {
                reduce {
                    state.copy(itemCreated = state.itemCreated.copy(category = category))
                }
            }

        fun onEditNewItemHotOrIce(hotOrIce: String) =
            intent {
                reduce {
                    state.copy(itemCreated = state.itemCreated.copy(hotOrIce = hotOrIce))
                }
            }

        fun onEditNewItemName(name: String) =
            intent {
                reduce {
                    state.copy(itemCreated = state.itemCreated.copy(name = name))
                }
            }

        fun onEditNewItemPrice(price: Int) =
            intent {
                reduce {
                    state.copy(itemCreated = state.itemCreated.copy(price = price))
                }
            }

        fun onEditNewItemDescription(description: String) =
            intent {
                reduce {
                    state.copy(itemCreated = state.itemCreated.copy(description = description))
                }
            }

        fun onEditNewItemImgPath(imgPath: String) =
            intent {
                reduce {
                    state.copy(itemCreated = state.itemCreated.copy(imgPath = imgPath))
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
    val itemCreated: MenuParam =
        MenuParam(
            category = "",
            hotOrIce = "",
            name = "",
            price = 0,
            description = "",
            imgPath = "",
        ),
)

sealed interface MenuManagementSideEffect {
    object NavigateToLoginScreen : MenuManagementSideEffect

    class Toast(
        val message: String,
    ) : MenuManagementSideEffect
}
