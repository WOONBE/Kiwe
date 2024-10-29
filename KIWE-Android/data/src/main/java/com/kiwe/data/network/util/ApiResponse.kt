package com.kiwe.data.network.util

sealed class ApiResponse<out D> {
    data class Success<out D>(
        val data: D,
    ) : ApiResponse<D>()

    sealed class Error(
        open val errorCode: String = "",
        open val errorMessage: String = "",
    ) : ApiResponse<Nothing>() {
        data class ServerError(
            override val errorCode: String = "",
            override val errorMessage: String = "",
        ) : Error(errorCode = errorCode, errorMessage = errorMessage)

        data class UnknownError(
            override val errorMessage: String = "",
        ) : Error(errorMessage = errorMessage)
    }
}
