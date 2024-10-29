package com.kiwe.data.exception

import com.kiwe.data.network.util.ErrorResponse
import java.io.IOException

class ApiException(
    override val message: String? = null,
    override val cause: Throwable? = null,
    val error: ErrorResponse,
): IOException(message, cause)