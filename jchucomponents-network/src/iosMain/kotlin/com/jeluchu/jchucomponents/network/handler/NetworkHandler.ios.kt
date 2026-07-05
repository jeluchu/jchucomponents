package com.jeluchu.jchucomponents.network.handler

import platform.Foundation.NSURLSessionConfiguration

actual class NetworkHandler {
    actual fun isNetworkAvailable(): Boolean =
        try {
            NSURLSessionConfiguration.defaultSessionConfiguration()
            true
        } catch (error: Throwable) {
            false
        }
}
