package com.kiwe.domain.usecase

import com.kiwe.domain.model.VoiceOrderRequest
import com.kiwe.domain.model.VoiceOrderResponse

interface VoiceOrderUseCase {
    suspend operator fun invoke(voiceOrder: VoiceOrderRequest): Result<VoiceOrderResponse>
}
