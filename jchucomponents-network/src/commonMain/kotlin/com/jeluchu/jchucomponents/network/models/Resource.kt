package com.jeluchu.jchucomponents.network.models

sealed class Resource<out E, out T>(
    open val error: E? = null,
    open val data: T? = null,
) {
    class Success<T>(data: T) : Resource<Nothing, T>(data = data)
    class Error<E, T>(error: E, data: T? = null) : Resource<E, T>(error = error, data = data)
    class Loading : Resource<Nothing, Nothing>()
}
