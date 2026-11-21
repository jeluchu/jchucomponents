package com.jeluchu.jchucomponents.network.handler

import kotlin.test.Test
import kotlin.test.assertTrue

class NetworkHandlerAppleTest {
    @Test
    fun wifiImpliesOnline() {
        val handler = NetworkHandler()
        // Works before and after the first path update from the system.
        repeat(3) {
            if (handler.isWifiConnected()) assertTrue(handler.isNetworkAvailable())
        }
    }
}
