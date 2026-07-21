package com.jeluchu.jchucomponents.supabase.flow

import com.jeluchu.jchucomponents.network.extensions.toFailure
import com.jeluchu.jchucomponents.network.models.Failure
import com.jeluchu.jchucomponents.network.models.Resource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

inline fun <T> supabaseResource(
    crossinline block: suspend () -> T
): Flow<Resource<Failure, T>> =
    flow<Resource<Failure, T>> {
        emit(Resource.Loading())
        emit(Resource.Success(block()))
    }.catch { throwable ->
        if (throwable is CancellationException) throw throwable
        emit(Resource.Error(throwable.toFailure()))
    }
