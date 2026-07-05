package com.jeluchu.jchucomponents.network.extensions

import com.jeluchu.jchucomponents.network.http.HttpStatusCode
import com.jeluchu.jchucomponents.network.models.Failure
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.CancellationException
import kotlinx.io.IOException

fun Throwable.toFailure(): Failure =
    when (this) {
        is CancellationException -> throw this
        is HttpRequestTimeoutException -> Failure.Timeout(message)
        is ResponseException ->
            Failure.ServerError(
                statusCode =
                    com.jeluchu.jchucomponents.network.http
                        .getHttpErrorInfo(response.status.value),
                errorMessage = message.orEmpty().ifBlank { response.status.description },
            )
        is IOException -> Failure.NetworkConnection(errorMessage = message.orEmpty().ifBlank { "Network connection failed" })
        else ->
            when {
                message?.contains("timeout", ignoreCase = true) == true -> Failure.Timeout(message)
                message?.contains("Unable to resolve host", ignoreCase = true) == true ->
                    Failure.NetworkConnection(errorMessage = "Unable to resolve host")
                message?.contains("SSL", ignoreCase = true) == true ->
                    Failure.NetworkConnection(errorMessage = "SSL connection failed")
                else -> Failure.UnknownError(message)
            }
    }

fun Failure?.handleFailure(): String =
    when (this) {
        is Failure.NetworkConnection -> "Network connection failed: $message"
        is Failure.ServerError -> code?.let { "HTTP $it: $message" } ?: message
        is Failure.DatabaseError,
        is Failure.CustomError,
        is Failure.LegacyError,
        is Failure.Timeout,
        is Failure.UnknownError,
        -> message
        null -> HttpStatusCode.Unknown.message
    }
