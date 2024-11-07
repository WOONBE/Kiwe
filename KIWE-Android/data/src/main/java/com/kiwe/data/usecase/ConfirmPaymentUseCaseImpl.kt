package com.kiwe.data.usecase

import com.kiwe.data.network.service.OrderService
import com.kiwe.domain.usecase.kiosk.ConfirmPaymentUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class ConfirmPaymentUseCaseImpl
    @Inject
    constructor(
        private val orderService: OrderService,
    ) : ConfirmPaymentUseCase {
        override suspend fun invoke(kioskId: Int): Result<String> {
            var job: Job? =
                CoroutineScope(Dispatchers.IO).launch {
                    orderService.confirmPayment(kioskId)
                }

            while (true) {
                try {
                    // PUT 요청을 보내고 응답을 받음

                    // 상태 코드가 200일 경우 성공 처리 후 반복 종료
                } catch (e: Exception) {
                    // 오류 처리 (예: 로그 출력)
                    Timber.tag(javaClass.simpleName).e("결제 확인 에러 발생 : $e")
                }
                // 500ms 대기 후 다음 요청을 보냄
                delay(500L)
            }

            val response = orderService.confirmPayment(kioskId)
            return Result.success(response.toString())
        }
    }
