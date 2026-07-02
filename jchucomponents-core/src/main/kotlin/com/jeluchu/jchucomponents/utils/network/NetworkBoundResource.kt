/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.utils.network

import com.jeluchu.jchucomponents.core.exception.Failure
import com.jeluchu.jchucomponents.utils.network.models.Resource
import java.io.IOException
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: () -> Boolean = { true },
    crossinline dbTransform: (ResultType) -> RequestType,
) = flow {

    emit(Resource.Loading())

    val flow = if (shouldFetch()) {
        try {
            saveFetchResult(fetch())
            query().mapToResource(transform = dbTransform)
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: IOException) {
            query().mapToResource(
                transform = dbTransform,
                errorMessage = Failure.NetworkConnection(errorMessage = exception.message.orEmpty())
            )
        } catch (error: ResponseException) {
            query().mapToResource(
                transform = dbTransform,
                errorMessage = Failure.ServerError(
                    errorCode = error.response.status.value,
                    errorMessage = error.message.orEmpty(),
                )
            )
        } catch (exception: Exception) {
            query().mapToResource(
                transform = dbTransform,
                errorMessage = Failure.NetworkConnection(errorMessage = exception.message.orEmpty())
            )
        }
    } else query().mapToResource(dbTransform)

    emitAll(flow)
}

inline fun <RequestType> networkResource(
    crossinline fetch: suspend () -> RequestType,
    crossinline shouldFetch: () -> Boolean = { true }
) = flow {

    emit(Resource.Loading())

    if (shouldFetch()) {

        try {
            emit(Resource.Success(fetch()))
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: IOException) {
            emit(Resource.Error(Failure.NetworkConnection(errorMessage = exception.message.orEmpty())))
        } catch (error: ResponseException) {
            emit(
                Resource.Error(
                    Failure.ServerError(
                        errorCode = error.response.status.value,
                        errorMessage = error.message.orEmpty(),
                    )
                )
            )
        } catch (exception: Exception) {
            emit(Resource.Error(Failure.NetworkConnection(errorMessage = exception.message.orEmpty())))
        }

    } else emit(Resource.Error(Failure.NetworkConnection(errorMessage = "error. .orEmpty()")))

}

inline fun <ResultType, RequestType> Flow<ResultType?>.mapToResource(
    crossinline transform: (ResultType) -> RequestType,
    errorMessage: Failure = Failure.DatabaseError("No data available")
): Flow<Resource<Failure, RequestType>> = map { result ->
    if (result != null) Resource.Success(data = transform(result))
    else Resource.Error(error = errorMessage)
}
