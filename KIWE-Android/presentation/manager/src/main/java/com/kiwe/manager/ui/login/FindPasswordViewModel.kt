package com.kiwe.manager.ui.login

import androidx.lifecycle.ViewModel
import com.kiwe.domain.exception.APIException
import com.kiwe.domain.model.EditMemberParam
import com.kiwe.domain.model.MenuCategory
import com.kiwe.domain.model.MenuParam
import com.kiwe.domain.usecase.manager.edit.EditMemberInfoUseCase
import com.kiwe.domain.usecase.manager.menu.CreateMenuUseCase
import com.kiwe.domain.usecase.manager.menu.DeleteMenuUseCase
import com.kiwe.domain.usecase.manager.menu.EditMenuUseCase
import com.kiwe.domain.usecase.manager.menu.GetAllMenuListUseCase
import com.kiwe.domain.usecase.manager.menu.GetMenuByIdUseCase
import com.kiwe.domain.usecase.manager.search.SearchAllMemberUseCase
import com.kiwe.domain.usecase.manager.search.SearchMemberByEmailUseCase
import com.kiwe.domain.usecase.manager.search.SearchMemberByIdUseCase
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
        private val searchMemberByIdUseCase: SearchMemberByIdUseCase,
        private val searchAllMemberUseCase: SearchAllMemberUseCase,
        private val editMemberInfoUseCase: EditMemberInfoUseCase,
        private val getMenuByIdUseCase: GetMenuByIdUseCase,
        private val getAllMenuListUseCase: GetAllMenuListUseCase,
        private val createMenuUseCase: CreateMenuUseCase,
        private val editMenuUseCase: EditMenuUseCase,
        private val deleteMenuUseCase: DeleteMenuUseCase,
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

        fun onSearchMemberByEmail() =
            intent {
                val response = searchMemberByEmailUseCase(state.searchMemberByEmail).getOrThrow()
                postSideEffect(FindPasswordSideEffect.Toast(response.toString()))
            }

        fun onSearchMemberById() =
            intent {
                val response = searchMemberByIdUseCase(state.id).getOrThrow()
                postSideEffect(FindPasswordSideEffect.Toast(response.toString()))
            }

        fun onSearchAllMember() =
            intent {
                val response = searchAllMemberUseCase().getOrThrow()
                postSideEffect(FindPasswordSideEffect.Toast(response.toString()))
            }

        fun onEditMemberInfo() =
            intent {
                val response =
                    editMemberInfoUseCase(
                        memberId = state.id,
                        newMemberInfo =
                            EditMemberParam(
                                name = state.name,
                                email = state.email,
                                kioskIds = listOf(),
                            ),
                    )
                postSideEffect(FindPasswordSideEffect.Toast(response.getOrThrow().toString()))
            }

        fun onGetMenuById() =
            intent {
                val response = getMenuByIdUseCase(state.id)
                postSideEffect(FindPasswordSideEffect.Toast(response.getOrThrow().toString()))
            }

        fun onGetAllMenu() =
            intent {
                val response = getAllMenuListUseCase()
                postSideEffect(FindPasswordSideEffect.Toast(response.getOrThrow().toString()))
            }

        fun onCreateMenu() =
            intent {
                val response =
                    createMenuUseCase(
                        createMenuParam =
                            MenuParam(
                                category = state.category,
                                hotOrIce = state.hotOrIce,
                                name = state.menuName,
                                price = state.price,
                                description = state.description,
                                imgPath = state.imgPath,
                            ),
                    ).getOrThrow()
                postSideEffect(FindPasswordSideEffect.Toast(response.toString()))
            }

        fun onEditMenu() =
            intent {
                val response =
                    editMenuUseCase(
                        menuId = state.menuId,
                        menuParam =
                            MenuParam(
                                category = state.category,
                                hotOrIce = state.hotOrIce,
                                name = state.newName,
                                price = state.price,
                                description = state.description,
                                imgPath = state.imgPath,
                            ),
                    ).getOrThrow()
                postSideEffect(FindPasswordSideEffect.Toast(response.toString()))
            }

        fun onDeleteMenu() =
            intent {
                val response = deleteMenuUseCase(state.menuId).getOrThrow()
                postSideEffect(FindPasswordSideEffect.Toast(response.toString()))
            }
    }

@Immutable
data class FindPasswordState(
    val searchMemberByEmail: String = "2",
    val id: Int = 3,
    val name: String = "2",
    val email: String = "2",
    val password: String = "2",
    val kioskIds: List<Int> = listOf(),
    val category: String = MenuCategory.NEW.displayName,
    val menuName: String = "새로운 음료 메뉴",
    val menuId: Int = 325,
    val hotOrIce: String = "ICE",
    val price: Int = 9990,
    val description: String = "음료입니다",
    val imgPath: String = "https://cdn-icons-png.flaticon.com/512/4329/4329542.png",
    val newName: String = "메뉴 이름 바꿔봤어요",
)

sealed interface FindPasswordSideEffect {
    object NavigateToLoginScreen : FindPasswordSideEffect

    class Toast(
        val message: String,
    ) : FindPasswordSideEffect
}
