package com.kiwe.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VoiceOrderResponse(
    val status: String,
    val data: VoiceBody,
)

@Serializable
data class VoiceBody(
    @SerialName("category")
    val category: Int, // AnswerType
    @SerialName("need_temp")
    val need_temp: Int,
    @SerialName("message")
    val message: String,
    @SerialName("order")
    val order: List<OrderList>,
    @SerialName("response")
    val response: String,
)

enum class AnswerType(
    val value: Int,
) {
    ORDER(1),
    UPDATE(2),
    SUGGESTION(3),
    EXPLANATION(4),
    ;

    fun fromInt(value: Int): AnswerType =
        entries.find { it.value == value }
            ?: throw IllegalArgumentException("Unknown AnswerType for value: $value")
}
