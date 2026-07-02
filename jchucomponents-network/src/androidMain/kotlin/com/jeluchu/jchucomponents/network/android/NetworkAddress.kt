package com.jeluchu.jchucomponents.network.android

import java.net.Inet4Address
import java.net.NetworkInterface

public fun getIpv4LocalHostAddress(): String {
    NetworkInterface.getNetworkInterfaces()?.toList()?.forEach { networkInterface ->
        networkInterface.inetAddresses
            ?.toList()
            ?.firstOrNull { !it.isLoopbackAddress && it is Inet4Address }
            ?.hostAddress
            ?.let { return it }
    }
    return ""
}
