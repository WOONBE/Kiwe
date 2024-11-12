package com.kiwe.domain.usecase

import com.kiwe.domain.model.VoiceOrderRequest
import com.kiwe.domain.model.VoiceTempResponse

interface VoiceOrderUseCase {
    suspend operator fun invoke(voiceOrder: VoiceOrderRequest): Result<VoiceTempResponse>
}
