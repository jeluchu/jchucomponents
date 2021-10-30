package com.jeluchu.jchucomponentscompose.utils.network

import com.jeluchu.jchucomponentscompose.core.exception.Failure
import com.jeluchu.jchucomponentscompose.utils.network.models.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

inline fun <ResultType, RequestType> networkBoundResourceNew(
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
            query().map { Resource.Success(dbTransform(it)) }
        } catch (exception: IOException) {
            query().map { Resource.Error(Failure.NetworkConnection(errorMessage = exception.message.orEmpty())) }
        } catch (error: HttpException) {
            query().map { Resource.Error(Failure.ServerError(error.code(), error.message())) }
        } catch (exception: Exception) {
            query().map { Resource.Error(Failure.NetworkConnection(errorMessage = exception.message.orEmpty())) }
        }

    } else query().map { Resource.Success(dbTransform(it)) }

    emitAll(flow)

}

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: () -> Boolean = { true }
) = flow {

    emit(Resource.Loading())

    val flow = if (shouldFetch()) {

        try {
            saveFetchResult(fetch())
            query().map { Resource.Success(it) }
        } catch (exception: IOException) {
            query().map { Resource.Error(Failure.ServerError(errorCode = 0, errorMessage = exception.message.orEmpty())) }
        } catch (error: HttpException) {
            query().map { Resource.Error(Failure.NetworkConnection(errorCode = error.code(), errorMessage = error.message.orEmpty())) }
        } catch (exception: Exception) {
            query().map { Resource.Error(Failure.ServerError(errorCode = 0, errorMessage = exception.message.orEmpty())) }
        }

    } else query().map { Resource.Success(it) }

    emitAll(flow)

}