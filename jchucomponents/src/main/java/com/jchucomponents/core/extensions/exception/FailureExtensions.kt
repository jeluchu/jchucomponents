/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jchucomponents.core.extensions.exception

import com.jchucomponents.core.exception.Failure

/**
 *
 * Extension of [Failure] that will help us to obtain more data about the error obtained,
 * either due to the Internet connection, the service or a custom error
 *
 */
fun Failure?.handleFailure() = when (this) {
    is Failure.NetworkConnection -> "Network Connection Failed: $errorMessage"
    is Failure.ServerError -> "Server Failed (Code: $errorCode): $errorMessage"
    is Failure.CustomError -> errorMessage
    else -> "Unknow Error"
}