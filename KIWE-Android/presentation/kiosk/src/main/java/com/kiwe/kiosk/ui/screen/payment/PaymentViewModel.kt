package com.kiwe.kiosk.ui.screen.payment

import androidx.compose.foundation.pager.PagerState
import androidx.lifecycle.viewModelScope
import com.kiwe.domain.model.Order
import com.kiwe.domain.usecase.kiosk.CancelPaymentUseCase
import com.kiwe.domain.usecase.kiosk.ConfirmPaymentUseCase
import com.kiwe.domain.usecase.kiosk.PostOrderUseCase
import com.kiwe.kiosk.base.BaseSideEffect
import com.kiwe.kiosk.base.BaseState
import com.kiwe.kiosk.base.BaseViewModel
import com.kiwe.kiosk.model.toOrderItem
import com.kiwe.kiosk.ui.screen.order.ShoppingCartState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
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
    ) : BaseViewModel<PaymentState, PaymentSideEffect>(PaymentState()) {
        private var confirmJob: Job? = null

        override fun handleExceptionIntent(
            coroutineContext: CoroutineContext,
            throwable: Throwable,
        ) {
            intent {
                postSideEffect(PaymentSideEffect.Toast(throwable.message ?: "알수 없는 에러"))
            }
        }

        // 페이지 전환 인텐트
        fun navigateToPaymentStatus(
            pagerState: PagerState,
            targetStatus: PaymentStatus,
        ) {
            viewModelScope.launch {
                withContext(Dispatchers.Main) {
                    // 페이지 전환 애니메이션
                    pagerState.scrollToPage(targetStatus.ordinal)
                }
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

        fun postOrder(
            kioskId: Int,
            shoppingCartState: ShoppingCartState,
        ) {
            val order =
                Order(
                    menuOrders =
                        shoppingCartState.shoppingCartItem.map {
                            it.toOrderItem()
                        },
                )
            viewModelScope.launch {
                runCatching {
                    postOrderUseCase(kioskId, order)
                }.onSuccess {
                    startConfirmPayment(kioskId)
                    Timber.tag(javaClass.simpleName).d("postOrder success")
                }.onFailure {
                    Timber.tag(javaClass.simpleName).e("postOrder error : $it")
                }
            }
            shoppingCartState.shoppingCartItem
            Timber.tag(javaClass.simpleName).d("postOrder")
            viewModelScope.launch {
                startConfirmPayment(kioskId)
            }
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
                    val limitTime = 20_000L // 30초 제한 시간 // TODO : 20초로 테스트
                    val interval = 500L // 0.5초 간격
                    while (System.currentTimeMillis() - startTime < limitTime) {
                        runCatching {
                            confirmPaymentUseCase(kioskId = kioskId)
                        }.onSuccess { result ->
                            val isPaymentConfirmed = result.getOrElse { false } // 실패 시 기본값 false

                            if (isPaymentConfirmed) {
                                Timber.tag(javaClass.simpleName).d("결제 성공")
                                viewModelScope.launch {
                                    successPayment(generateUserCardNumber())
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
            val currentTime = System.currentTimeMillis()
            val dateFormat = SimpleDateFormat("MMdd mmss", Locale.getDefault())
            val formattedTime = dateFormat.format(Date(currentTime))

            // 카드 번호 형식 적용
            val cardNumber = "$formattedTime **** ****"
            return cardNumber
        }

        override fun onCleared() {
            hideDialog()
            super.onCleared()
            confirmJob?.cancel()
        }
    }

data class PaymentState(
    val kioskId: Int = 1,
    val order: Order? = null,
    val showDialog: Boolean = false,
    val userCardNumber: String = "",
    val completePayment: Boolean = false,
) : BaseState

sealed interface PaymentSideEffect : BaseSideEffect {
    data class Toast(
        val message: String,
    ) : PaymentSideEffect
}
