package com.kiwe.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class EditMemberParam(
    val name: String,
    val email: String,
    val kioskIds: List<Int>,
)
