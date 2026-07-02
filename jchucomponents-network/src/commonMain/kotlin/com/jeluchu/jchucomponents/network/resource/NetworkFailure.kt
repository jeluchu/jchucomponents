package com.jeluchu.jchucomponents.network.resource

sealed class NetworkFailure {
    data class DatabaseError(val errorMessage: String? = null) : NetworkFailure()
    data class ServerError(val errorCode: Int, val errorMessage: String) : NetworkFailure()
    data class NetworkConnection(
        val errorCode: Int? = null,
        val errorMessage: String,
    ) : NetworkFailure()
}
