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
        throwable: Throwable
    ) {
        intent {
            postSideEffect(PaymentSideEffect.Toast(throwable.message ?: "알수 없는 에러"))
        }
    }

    // 페이지 전환 인텐트
    fun navigateToPaymentStatus(pagerState: PagerState, targetStatus: PaymentStatus) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                // 페이지 전환 애니메이션
                pagerState.scrollToPage(targetStatus.ordinal)
            }
        }
    }

    fun postOrder(shoppingCartState: ShoppingCartState) {
        val order =
            Order(
                menuOrders =
                shoppingCartState.shoppingCartItem.map {
                    it.toOrderItem()
                },
            )
        shoppingCartState.shoppingCartItem
        Timber.tag(javaClass.simpleName).d("postOrder")
        viewModelScope.launch {
            postOrderUseCase(order)
        }
    }

    fun startConfirmPayment(kioskId: Int) {
        // 기존 job이 활성화되어 있다면 취소
        confirmJob?.cancel()

        // 새로운 job을 생성하여 결제 확인 작업 시작
        confirmJob = viewModelScope.launch {
            val startTime = System.currentTimeMillis()
            val limitTime = 30_000L // 30초 제한 시간
            val interval = 500L // 0.5초 간격
            while (System.currentTimeMillis() - startTime < limitTime) {
                runCatching {
                    confirmPaymentUseCase(kioskId = kioskId)
                }.onSuccess { result ->
                    val isPaymentConfirmed = result.getOrElse { false } // 실패 시 기본값 false

                    if (isPaymentConfirmed) {
                        Timber.tag(javaClass.simpleName).d("결제 성공")
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
        }
    }

    override fun onCleared() {
        super.onCleared()
        confirmJob?.cancel()
    }
}

data class PaymentState(
    val order: Order? = null,
) : BaseState

sealed interface PaymentSideEffect : BaseSideEffect {
    data class Toast(
        val message: String,
    ) : PaymentSideEffect
}
