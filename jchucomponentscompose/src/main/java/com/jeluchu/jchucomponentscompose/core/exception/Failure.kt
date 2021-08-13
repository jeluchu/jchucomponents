package com.jeluchu.jchucomponentscompose.core.exception

sealed class Failure {
    object NetworkConnection : Failure()
    object ServerError : Failure()
    data class CustomError(val errorCode: Int, val errorMessage: String?) : Failure()
}