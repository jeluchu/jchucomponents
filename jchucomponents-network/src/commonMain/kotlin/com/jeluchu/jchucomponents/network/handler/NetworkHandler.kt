package com.jeluchu.jchucomponents.network.handler

@Suppress("KotlinNoActualForExpect")
expect class NetworkHandler {
    fun isNetworkAvailable(): Boolean
}
