package com.jeluchu.jchucomponentscompose.core.exception

import com.jeluchu.jchucomponentscompose.core.extensions.ints.empty
import com.jeluchu.jchucomponentscompose.core.extensions.strings.empty

sealed class Failure {
    data class NetworkConnection(val errorCode: Int? = null, val errorMessage: String) : Failure()
    data class ServerError(val errorCode: Int? = Int.empty(), val errorMessage: String? = String.empty()) : Failure()
    data class CustomError(val errorCode: Int, val errorMessage: String) : Failure()
}