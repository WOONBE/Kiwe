package com.kiwe.manager.ui.kioskmanagement

import androidx.lifecycle.ViewModel
import com.kiwe.domain.exception.APIException
import com.kiwe.domain.model.Kiosk
import com.kiwe.domain.usecase.manager.kiosk.GetAllMyKioskUseCase
import com.kiwe.domain.usecase.manager.search.SearchMyInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

@HiltViewModel
class KioskManagementViewModel
    @Inject
    constructor(
        private val getAllMyKioskUseCase: GetAllMyKioskUseCase,
        private val searchMyInfoUseCae: SearchMyInfoUseCase,
        // private val deleteKioskUseCase: DeleteKioskUseCase,
    ) : ViewModel(),
        ContainerHost<KioskManagementState, KioskManagementSideEffect> {
        override val container: Container<KioskManagementState, KioskManagementSideEffect> =
            container(
                initialState = KioskManagementState(),
                buildSettings = {
                    this.exceptionHandler =
                        CoroutineExceptionHandler { _, throwable ->
                            intent {
                                if (throwable is APIException) {
                                    postSideEffect(
                                        KioskManagementSideEffect.Toast(
                                            "${throwable.code} : " +
                                                (throwable.message ?: "알수 없는 에러"),
                                        ),
                                    )
                                } else {
                                    postSideEffect(
                                        KioskManagementSideEffect.Toast(
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
                val myKiosk = getAllMyKioskUseCase().getOrThrow()
                val myInfo = searchMyInfoUseCae().getOrThrow()
                reduce {
                    state.copy(kioskList = myKiosk, ownerId = myInfo.id)
                }
            }
        }

        fun onKioskDelete(kioskId: Int) =
            intent {
                // val response = deleteKioskUseCase(kioskId = kioskId)
                // val kioskId = kioskId
                postSideEffect(KioskManagementSideEffect.Toast("삭제에 성공했습니다"))
                val newList = getAllMyKioskUseCase().getOrThrow()
                reduce {
                    state.copy(kioskList = newList)
                }
            }

//        fun onHideKioskD
    }

@Immutable
data class KioskManagementState(
    val ownerId: Int = -1,
    val kioskList: List<Kiosk> = emptyList(),
)

sealed interface KioskManagementSideEffect {
    object ShowEditDialog : KioskManagementSideEffect

    class Toast(
        val message: String,
    ) : KioskManagementSideEffect
}
