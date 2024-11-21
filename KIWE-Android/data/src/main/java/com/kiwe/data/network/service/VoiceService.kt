package com.kiwe.data.network.service

import com.kiwe.data.di.Fast
import com.kiwe.data.network.util.postResult
import com.kiwe.domain.model.VoiceOrderRequest
import com.kiwe.domain.model.VoiceOrderResponse
import com.kiwe.domain.model.VoiceRecommendRequest
import com.kiwe.domain.model.VoiceRecommendResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import javax.inject.Inject

class VoiceService
    @Inject
    constructor(
        @Fast
        private val client: HttpClient,
    ) {
        suspend fun postVoiceOrder(request: VoiceOrderRequest): Result<VoiceOrderResponse> =
            client.postResult("order") {
                setBody(request)
            }

        suspend fun postVoiceRecommend(request: VoiceRecommendRequest): Result<VoiceRecommendResponse> =
            client.postResult("recommend") {
                setBody(request)
            }
    }
