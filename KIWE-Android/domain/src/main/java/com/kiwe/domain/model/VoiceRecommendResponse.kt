package com.kiwe.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class VoiceRecommendResponse(
    val status: String,
    val message: String,
)
