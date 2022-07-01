/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jchucomponents.core.extensions.retrofit

import com.jchucomponents.core.exception.Failure
import com.jchucomponents.core.functional.Either
import retrofit2.Call

fun <T, R> request(
    call: Call<T>,
    transform: (T) -> R,
    default: T
): Either<Failure, R> {
    return runCatching {
        val response = call.execute()
        when (response.isSuccessful) {
            true -> Either.Right(transform((response.body() ?: default)))
            false -> Either.Left(Failure.LegacyError(response.code(), response.message()))
        }
    }.getOrElse { Either.Left(Failure.LegacyError()) }
}
