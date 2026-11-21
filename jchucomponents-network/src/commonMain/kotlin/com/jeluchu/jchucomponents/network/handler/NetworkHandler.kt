package com.jeluchu.jchucomponents.network.handler

/** Connectivity checks shared by the apps. Android needs the `ACCESS_NETWORK_STATE` permission. */
@Suppress("KotlinNoActualForExpect")
expect class NetworkHandler {
    /** True when the device has a usable network (Wi-Fi, mobile data, Ethernet...). */
    fun isNetworkAvailable(): Boolean

    /**
     * True when the connection is Wi-Fi or Ethernet, so large downloads do not use mobile data.
     * On JVM desktop there is no mobile data and this is the same as [isNetworkAvailable].
     */
    fun isWifiConnected(): Boolean
}
