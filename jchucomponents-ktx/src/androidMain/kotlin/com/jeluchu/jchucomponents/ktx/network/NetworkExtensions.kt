package com.jeluchu.jchucomponents.ktx.network

import com.jeluchu.jchucomponents.network.extensions.getIpv4LocalHostAddress as networkIpv4Address

@Deprecated("Use getIpv4LocalHostAddress from jchucomponents-network")
fun getIpv4LocalHostAddress(): String = networkIpv4Address()
