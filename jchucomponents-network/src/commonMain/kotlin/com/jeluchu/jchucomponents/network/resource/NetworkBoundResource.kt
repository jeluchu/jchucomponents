package com.jeluchu.jchucomponents.network.resource

import com.jeluchu.jchucomponents.network.extensions.toFailure
import com.jeluchu.jchucomponents.network.models.Failure
import com.jeluchu.jchucomponents.network.models.Resource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.io.IOException

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType?>,
    crossinline fetch: suspend () -> RequestType,
    crossinline dbTransform: (ResultType) -> RequestType,
    crossinline shouldFetch: suspend () -> Boolean = { true },
    crossinline saveFetchResult: suspend (RequestType) -> Unit
) = flow {
    emit(value = Resource.Loading())

    val flow = if (shouldFetch()) {
        try {
            saveFetchResult(fetch())
            query().mapToResource(transform = dbTransform)
        } catch (exception: IOException) {
            query().mapToResource(
                transform = dbTransform,
                failure = exception.toFailure()
            )
        } catch (error: Exception) {
            query().mapToResource(
                transform = dbTransform,
                failure = error.toFailure()
            )
        }
    } else query().mapToResource(dbTransform)

    emitAll(flow)
}

inline fun <RequestType> networkResource(
    crossinline fetch: suspend () -> RequestType,
    crossinline shouldFetch: () -> Boolean = { true }
) = flow {
    emit(value = Resource.Loading())

    if (shouldFetch()) {
        try {
            emit(value = Resource.Success(data = fetch()))
        } catch (exception: IOException) {
            emit(value = Resource.Error(error = exception.toFailure()))
        } catch (error: Exception) {
            emit(value = Resource.Error(error = error.toFailure()))
        }
    } else {
        emit(value = Resource.Error(error = Failure.NetworkConnection(errorMessage = "Fetch skipped")))
    }
}

inline fun <ResultType, RequestType> Flow<ResultType?>.mapToResource(
    crossinline transform: (ResultType) -> RequestType,
    failure: Failure? = null,
): Flow<Resource<Failure, RequestType>> = map { result ->
    val data = result?.let(block = transform)

    when {
        failure != null -> Resource.Error(error = failure, data = data)
        data != null -> Resource.Success(data = data)
        else -> Resource.Error(error = Failure.DatabaseError())
    }
}.catch { exception ->
    if (exception is CancellationException) throw exception
    emit(value = Resource.Error(error = exception.toFailure()))
}
