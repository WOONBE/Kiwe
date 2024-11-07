package com.kiwe.domain.exception

data class APIException(
    override val message: String,
    val code: Int,
    val throwable: Throwable,
) : Exception(message, throwable)
