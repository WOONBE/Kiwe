package com.kiwe.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class VoiceOrderResponse(
    val category: Int, // AnswerType
    val need_temp: Boolean,
    val message: String,
    val order: List<OrderList>,
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
