package com.kiwe.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class VoiceRecommendRequest(
    val sentence: String,
    val age: Int,
)
