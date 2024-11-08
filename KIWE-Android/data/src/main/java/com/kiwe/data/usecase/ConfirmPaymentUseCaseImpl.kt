package com.kiwe.data.usecase

import com.kiwe.data.network.service.OrderService
import com.kiwe.domain.usecase.kiosk.ConfirmPaymentUseCase
import timber.log.Timber
import javax.inject.Inject

class ConfirmPaymentUseCaseImpl
    @Inject
    constructor(
        private val orderService: OrderService,
    ) : ConfirmPaymentUseCase {
        override suspend fun invoke(kioskId: Int): Result<Boolean> =
            runCatching {
                // 서버에 결제 확인 요청
                val response = orderService.confirmPayment(kioskId)
                if (response) {
                    Timber.tag(javaClass.simpleName).d("결제 확인 성공")
                } else {
                    Timber.tag(javaClass.simpleName).d("결제 확인 실패")
                }
                response // true 또는 false 반환
            }.onFailure { e ->
                Timber.tag(javaClass.simpleName).e("결제 확인 중 에러 발생 : $e")
            }
    }
