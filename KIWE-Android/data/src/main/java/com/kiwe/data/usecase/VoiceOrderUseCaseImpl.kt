package com.kiwe.data.usecase

import com.kiwe.data.network.service.VoiceService
import com.kiwe.domain.model.VoiceOrderRequest
import com.kiwe.domain.model.VoiceTempResponse
import com.kiwe.domain.usecase.VoiceOrderUseCase
import timber.log.Timber
import javax.inject.Inject

class VoiceOrderUseCaseImpl
    @Inject
    constructor(
        private val voiceService: VoiceService,
    ) : VoiceOrderUseCase {
        override suspend fun invoke(voiceOrder: VoiceOrderRequest): Result<VoiceTempResponse> {
            val response = voiceService.postVoiceOrder(voiceOrder)
            Timber.tag("VoiceOrderUseCaseImpl").d("response: $response")
            return response
        }
    }
