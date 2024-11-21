package com.kiwe.data.usecase

import com.kiwe.data.network.service.VoiceService
import com.kiwe.domain.model.VoiceRecommendRequest
import com.kiwe.domain.model.VoiceRecommendResponse
import com.kiwe.domain.usecase.VoiceRecommendUseCase
import timber.log.Timber
import javax.inject.Inject

class VoiceRecommendUseCaseImpl
    @Inject
    constructor(
        private val voiceService: VoiceService,
    ) : VoiceRecommendUseCase {
        override suspend fun invoke(voiceRecommend: VoiceRecommendRequest): Result<VoiceRecommendResponse> {
            val response = voiceService.postVoiceRecommend(voiceRecommend)
            Timber.tag("VoiceRecommendUseCase").d("response: $response")
            return response
        }
    }
