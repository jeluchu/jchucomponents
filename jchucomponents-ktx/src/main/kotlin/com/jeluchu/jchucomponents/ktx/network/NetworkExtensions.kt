package com.jeluchu.jchucomponents.ktx.network

import com.jeluchu.jchucomponents.ktx.strings.empty
import java.net.Inet4Address
import java.net.NetworkInterface

fun getIpv4LocalHostAddress(): String {
    val ip = String.empty()
    NetworkInterface.getNetworkInterfaces()?.toList()?.map { networkInterface ->
        networkInterface.inetAddresses
            ?.toList()
            ?.find { !it.isLoopbackAddress && it is Inet4Address }
            ?.let { return it.hostAddress.orEmpty() }
    }
    return ip
}
