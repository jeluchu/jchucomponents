package com.jeluchu.jchucomponentscompose.core.extensions.retrofit

import com.jeluchu.jchucomponentscompose.core.functional.Either
import com.jeluchu.jchucomponentscompose.core.exception.Failure
import retrofit2.Call

fun <T, R> request(
    call: Call<T>,
    transform: (T) -> R,
    default: T,
): Either<Failure, R> {
    return try {
        val response = call.execute()
        when (response.isSuccessful) {
            true -> Either.Right(transform((response.body() ?: default)))
            false -> {

                when (response.code()) {
                    StatusCode.InternalServerError.code -> Either.Left(Failure.ServerError)
                    StatusCode.BadGateway.code -> Either.Left(Failure.ServerError)
                    else -> Either.Left(Failure.ServerError)
                }

            }
        }
    } catch (exception: Throwable) {
        Either.Left(Failure.ServerError)
    }
}
