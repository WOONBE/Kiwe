package com.kiwe.kiosk.ui.screen.payment

import androidx.lifecycle.viewModelScope
import com.kiwe.domain.model.Order
import com.kiwe.domain.usecase.kiosk.CancelPaymentUseCase
import com.kiwe.domain.usecase.kiosk.ConfirmPaymentUseCase
import com.kiwe.domain.usecase.kiosk.PostOrderUseCase
import com.kiwe.domain.usecase.kiosk.datasource.GetKioskIdUseCase
import com.kiwe.domain.usecase.kiosk.datasource.GetKioskNameUseCase
import com.kiwe.domain.usecase.kiosk.datasource.GetOrderNumberUseCase
import com.kiwe.kiosk.base.BaseSideEffect
import com.kiwe.kiosk.base.BaseState
import com.kiwe.kiosk.base.BaseViewModel
import com.kiwe.kiosk.model.toOrderItem
import com.kiwe.kiosk.ui.screen.order.ShoppingCartState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class PaymentViewModel
    @Inject
    constructor(
        private val postOrderUseCase: PostOrderUseCase,
        private val confirmPaymentUseCase: ConfirmPaymentUseCase,
        private val cancelPaymentUseCase: CancelPaymentUseCase,
        private val getKioskIdUseCase: GetKioskIdUseCase,
        private val getKioskNameUseCase: GetKioskNameUseCase,
        private val getOrderNumberUseCase: GetOrderNumberUseCase,
    ) : BaseViewModel<PaymentState, PaymentSideEffect>(PaymentState()) {
        private var confirmJob: Job? = null

        init {
            intent {
                val getKioskId = getKioskIdUseCase()?.toIntOrNull() ?: 1
                Timber.tag("그바르디올").d("${javaClass.simpleName} :키오ID $getKioskId")
                reduce {
                    state.copy(kioskId = getKioskId)
                }
            }
        }

        override fun handleExceptionIntent(
            coroutineContext: CoroutineContext,
            throwable: Throwable,
        ) {
            intent {
                postSideEffect(PaymentSideEffect.Toast(throwable.message ?: "알수 없는 에러"))
            }
        }

        // 다이얼로그 표시 상태 설정 함수
        fun showDialog() {
            intent {
                reduce { state.copy(showDialog = true) }
            }
        }

        fun hideDialog() {
            intent {
                reduce { state.copy(showDialog = false) }
            }
        }

        fun initUserCardNumber() {
            intent {
                reduce { state.copy(userCardNumber = "") }
            }
        }

        fun successPayment(cardNumber: String) {
            intent {
                reduce { state.copy(userCardNumber = cardNumber) }
            }
        }

        fun completePayment() {
            intent {
                reduce { state.copy(completePayment = true) }
            }
        }

        fun setAgeAndGender(
            age: Int,
            gender: String,
        ) {
            intent {
                reduce { state.copy(age = age, gender = gender) }
            }
        }

        fun createOrderNumber() {
            intent {
                val kioskName = getKioskNameUseCase()
                val orderNumber =
                    String.format(Locale.KOREAN, "%03d", getOrderNumberUseCase()) // 3자리로 포맷팅
                reduce {
                    state.copy(
                        orderNumber = "$kioskName$orderNumber",
                    )
                }
                Timber.tag("그바르디올").d("${javaClass.simpleName} : 메서드 $kioskName$orderNumber")
            }
        }

        fun postOrder(
            kioskId: Int,
            age: Int,
            gender: String,
            shoppingCartState: ShoppingCartState,
        ) {
            intent {
                viewModelScope.launch {
                    runCatching {
                        val kioskName = getKioskNameUseCase()
                        val orderNumber =
                            String.format(Locale.KOREAN, "%03d", getOrderNumberUseCase()) // 3자리로 포맷팅
                        reduce {
                            state.copy(
                                orderNumber = "$kioskName$orderNumber",
                            )
                        }
                        val order =
                            Order(
                                menuOrders =
                                    shoppingCartState.shoppingCartItem.map {
                                        it.toOrderItem()
                                    },
                            )
                        postOrderUseCase(kioskId, age, gender, state.orderNumber.toInt(), order)
                    }.onSuccess {
                        startConfirmPayment(kioskId)
                        Timber.tag(javaClass.simpleName).d("postOrder success")
                    }.onFailure {
                        Timber.tag(javaClass.simpleName).e("postOrder error : $it")
                    }
                }
                Timber.tag(javaClass.simpleName).d("postOrder")
            }

//            intent {
//                createOrderNumber()
//                val order =
//                    Order(
//                        menuOrders =
//                        shoppingCartState.shoppingCartItem.map {
//                            it.toOrderItem()
//                        },
//                    )
//                postOrderUseCase(kioskId, age, gender, state.orderNumber.toInt(), order)
//            }
        }

        fun startConfirmPayment(kioskId: Int) {
            // 기존 job이 활성화되어 있다면 취소
            confirmJob?.cancel()

            // 새로운 job을 생성하여 결제 확인 작업 시작
            confirmJob =
                viewModelScope.launch {
                    initUserCardNumber()
                    showDialog()
                    val startTime = System.currentTimeMillis()
                    val limitTime = 30_000L // 30초 제한 시간
                    val interval = 500L // 0.5초 간격

                    while (System.currentTimeMillis() - startTime < limitTime) {
                        intent {
                            reduce { state.copy(remainingTime = (System.currentTimeMillis() - startTime) / 1000) }
                        }
                        runCatching {
                            confirmPaymentUseCase(kioskId = kioskId)
                        }.onSuccess { result ->
                            Timber.tag("결제 요청").d("result $result")
                            val isPaymentConfirmed = result.getOrElse { false } // 실패 시 기본값 false

                            if (isPaymentConfirmed) {
                                Timber.tag(javaClass.simpleName).d("결제 성공")
                                viewModelScope.launch {
                                    successPayment(generateUserCardNumber())
                                    navigateToReceiptScreen()
                                    delay(1000L)
                                    completePayment()
                                    hideDialog() // 결제가 성공하면 다이얼로그 숨김
                                }
                                return@launch
                            } else {
                                Timber.tag(javaClass.simpleName).d("결제 대기 중...")
                            }
                        }.onFailure { e ->
                            Timber.tag(javaClass.simpleName).e("결제 확인 에러 : $e")
                        }
                        delay(interval) // 다음 확인 요청 전 대기
                    }

                    // 제한 시간 초과 시 처리
                    if (System.currentTimeMillis() - startTime >= limitTime) {
                        Timber.tag(javaClass.simpleName).d("결제 확인 시간 초과")
                        cancelPayment()
                    }
                }
        }

        fun navigateToReceiptScreen() {
            // createOrderNumber 함수를 호출하여 orderNumber를 생성한 뒤, 이를 ReceiptScreen으로 전달
            intent {
                repeat(5) {
                    if (state.orderNumber.isBlank()) {
                        delay(500L)
                        Timber.tag("그바르디올").d("${javaClass.simpleName} : 아직 비어 있음 ${state.orderNumber}")
                    } else {
                        postSideEffect(PaymentSideEffect.NavigateToReceiptScreen(orderNumber = state.orderNumber))
                        return@repeat // 반복 종료
                    }
                }
                Timber.tag("그바르디올").d("${javaClass.simpleName} : ${state.orderNumber}")
            }
        }

        fun cancelPayment() {
            Timber.tag(javaClass.simpleName).d("cancelPayment")
            // confirmJob이 실행 중이면 취소
            confirmJob?.cancel()
            confirmJob = null
            viewModelScope.launch {
                cancelPaymentUseCase(kioskId = 1)
                hideDialog()
            }
        }

        fun generateUserCardNumber(): String {
            val randomPrefix = (1000..9999).random()
            val randomSuffix = (1000..9999).random()
            // 카드 번호 형식 적용
            return "$randomPrefix $randomSuffix **** ****"
        }

        override fun onCleared() {
            hideDialog()
            super.onCleared()
            confirmJob?.cancel()
        }
    }

data class PaymentState(
    val kioskId: Int = 1, // FIXME : DataStore로 변경
    val age: Int = 30,
    val gender: String = "Male",
    val order: Order? = null,
    val showDialog: Boolean = false,
    val remainingTime: Long = 0,
    val userCardNumber: String = "",
    val completePayment: Boolean = false,
    val orderNumber: String = "2005",
) : BaseState

sealed interface PaymentSideEffect : BaseSideEffect {
    data class Toast(
        val message: String,
    ) : PaymentSideEffect

    data class NavigateToReceiptScreen(
        val orderNumber: String,
    ) : PaymentSideEffect
}
