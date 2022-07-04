/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponentscompose.core.exception

sealed class Failure {
    data class NetworkConnection(val errorCode: Int? = null, val errorMessage: String) : Failure()
    data class ServerError(val errorCode: Int, val errorMessage: String) : Failure()
    data class CustomError(val errorCode: Int, val errorMessage: String) : Failure()
    data class LegacyError(val errorCode: Int? = null, val errorMessage: String? = null) : Failure()
}