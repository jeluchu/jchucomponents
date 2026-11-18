package com.jeluchu.jchucomponents.network.handler

import java.net.NetworkInterface

actual class NetworkHandler {
    /** True when at least one non-loopback network interface is up and has an address. */
    actual fun isNetworkAvailable(): Boolean =
        runCatching {
            NetworkInterface.getNetworkInterfaces()?.toList().orEmpty().any { networkInterface ->
                networkInterface.isUp &&
                    !networkInterface.isLoopback &&
                    networkInterface.inetAddresses.hasMoreElements()
            }
        }.getOrDefault(false)
}
