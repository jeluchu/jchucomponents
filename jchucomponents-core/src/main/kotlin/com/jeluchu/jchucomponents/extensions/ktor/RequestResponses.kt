/*
 *
 *  Copyright 2026 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.extensions.ktor

import com.jeluchu.jchucomponents.core.exception.Failure
import com.jeluchu.jchucomponents.core.functional.Either
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.CancellationException

/**
 * Executes a Ktor request and maps its successful body to the domain result.
 */
public suspend inline fun <reified T, R> request(
    crossinline call: suspend () -> HttpResponse,
    transform: (T) -> R,
    default: T,
): Either<Failure, R> {
    return try {
        val response = call()
        if (response.status.value in 200..299) {
            val body = try {
                response.body<T>()
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                default
            }
            Either.Right(transform(body))
        } else {
            Either.Left(
                Failure.LegacyError(
                    errorCode = response.status.value,
                    errorMessage = response.status.description,
                )
            )
        }
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: ResponseException) {
        Either.Left(
            Failure.LegacyError(
                errorCode = exception.response.status.value,
                errorMessage = exception.message,
            )
        )
    } catch (exception: Exception) {
        Either.Left(Failure.LegacyError(errorMessage = exception.message))
    }
}
