package com.jeluchu.jchucomponents.network.handler

import kotlin.test.Test
import kotlin.test.assertEquals

class NetworkHandlerJvmTest {
    @Test
    fun wifiIsTheSameAsAvailableOnDesktop() {
        val handler = NetworkHandler()
        assertEquals(handler.isNetworkAvailable(), handler.isWifiConnected())
    }
}
