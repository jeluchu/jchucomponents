package com.jeluchu.jchucomponents.network.extensions.flow

import com.jeluchu.jchucomponents.network.extensions.toFailure
import com.jeluchu.jchucomponents.network.models.Failure
import com.jeluchu.jchucomponents.network.models.Resource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

/** Thrown by the `await*` helpers when the awaited [Resource] ends in [Resource.Error]. */
class ResourceException(
    val failure: Failure
) : RuntimeException(failure.message)

/** Waits for the first result that is not a [Resource.Loading] placeholder. */
suspend fun <E, T> Flow<Resource<E, T>>.awaitResource(): Resource<E, T> = first { it !is Resource.Loading }

/**
 * Waits for the terminal result and returns its data, or `null` when it carries none.
 *
 * @throws ResourceException when the result is a [Resource.Error]
 */
suspend fun <T> Flow<Resource<Failure, T>>.awaitResourceData(): T? =
    when (val result = awaitResource()) {
        is Resource.Success -> result.data
        is Resource.Error -> throw ResourceException(result.error ?: Failure.UnknownError())
        is Resource.Loading -> null
    }

/**
 * Waits for the terminal result and returns its data.
 *
 * @throws ResourceException when the result is a [Resource.Error] or carries no data
 */
suspend fun <T> Flow<Resource<Failure, T>>.awaitRequiredResourceData(): T =
    awaitResourceData() ?: throw ResourceException(Failure.UnknownError())

/**
 * Waits for a mutation that returns no data.
 *
 * @throws ResourceException when the result is a [Resource.Error]
 */
suspend fun Flow<Resource<Failure, Unit>>.awaitResourceUnit() {
    awaitResourceData()
}

/** Turns a plain flow into [Resource.Loading], then a [Resource.Success] per value or a final [Resource.Error]. */
fun <T> Flow<T>.asResourceFlow(): Flow<Resource<Failure, T>> =
    flow {
        emit(Resource.Loading())
        try {
            emitAll(map { Resource.Success(it) })
        } catch (error: Throwable) {
            if (error is CancellationException) throw error
            emit(Resource.Error(error = error.toFailure()))
        }
    }

/** Turns anything the upstream throws into [Resource.Error], so collectors report it instead of crashing. */
fun <T> Flow<Resource<Failure, T>>.catchAsResourceError(): Flow<Resource<Failure, T>> =
    catch { error ->
        if (error is CancellationException) throw error
        emit(Resource.Error(error = error.toFailure()))
    }
