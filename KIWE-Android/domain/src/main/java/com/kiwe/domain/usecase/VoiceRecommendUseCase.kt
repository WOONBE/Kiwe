package com.kiwe.domain.usecase

import com.kiwe.domain.model.VoiceRecommendRequest
import com.kiwe.domain.model.VoiceRecommendResponse

interface VoiceRecommendUseCase {
    suspend operator fun invoke(voiceRecommend: VoiceRecommendRequest): Result<VoiceRecommendResponse>
}
