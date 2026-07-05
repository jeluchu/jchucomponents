package com.jeluchu.jchucomponents.network.models

import com.jeluchu.jchucomponents.network.http.HttpStatusCode
import com.jeluchu.jchucomponents.network.http.getHttpErrorInfo

sealed class Failure {
    abstract val code: Int?
    abstract val message: String

    data class DatabaseError(
        val errorMessage: String? = null,
    ) : Failure() {
        override val code: Int? = null
        override val message: String = errorMessage.orEmpty().ifBlank { "No data available" }
    }

    data class ServerError(
        val statusCode: HttpStatusCode,
        val errorMessage: String? = null,
    ) : Failure() {
        constructor(errorCode: Int, errorMessage: String? = null) : this(
            statusCode = getHttpErrorInfo(errorCode),
            errorMessage = errorMessage,
        )

        override val code: Int? = statusCode.code.takeUnless { it == HttpStatusCode.Unknown.code }
        override val message: String = errorMessage.orEmpty().ifBlank { statusCode.message }
    }

    data class CustomError(
        val errorCode: Int? = null,
        val errorMessage: String,
    ) : Failure() {
        override val code: Int? = errorCode
        override val message: String = errorMessage
    }

    data class NetworkConnection(
        val errorCode: Int? = null,
        val errorMessage: String,
    ) : Failure() {
        override val code: Int? = errorCode
        override val message: String = errorMessage.ifBlank { "Network connection failed" }
    }

    data class Timeout(
        val errorMessage: String? = null,
    ) : Failure() {
        override val code: Int = HttpStatusCode.RequestTimeout.code
        override val message: String = errorMessage.orEmpty().ifBlank { HttpStatusCode.RequestTimeout.message }
    }

    data class UnknownError(
        val errorMessage: String? = null,
    ) : Failure() {
        override val code: Int? = null
        override val message: String = errorMessage.orEmpty().ifBlank { HttpStatusCode.Unknown.message }
    }

    data class LegacyError(
        val errorCode: Int? = null,
        val errorMessage: String? = null,
    ) : Failure() {
        override val code: Int? = errorCode
        override val message: String = errorMessage.orEmpty().ifBlank { HttpStatusCode.Unknown.message }
    }
}
