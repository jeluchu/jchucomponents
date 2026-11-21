package com.jeluchu.jchucomponents.network.handler

import platform.Network.nw_interface_type_wifi
import platform.Network.nw_interface_type_wired
import platform.Network.nw_path_get_status
import platform.Network.nw_path_monitor_create
import platform.Network.nw_path_monitor_set_queue
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_monitor_start
import platform.Network.nw_path_status_satisfied
import platform.Network.nw_path_uses_interface_type
import platform.darwin.dispatch_queue_create
import kotlin.concurrent.Volatile

/**
 * Connectivity of iOS and macOS, tracked with the Network framework. Until the system reports the first path
 * (a few milliseconds after the first use) the device is assumed to be online, as it was before.
 */
actual class NetworkHandler {
    actual fun isNetworkAvailable(): Boolean = NetworkPathState.isOnline

    actual fun isWifiConnected(): Boolean = NetworkPathState.isOnline && NetworkPathState.isWifiOrWired
}

private object NetworkPathState {
    @Volatile
    var isOnline: Boolean = true
        private set

    @Volatile
    var isWifiOrWired: Boolean = true
        private set

    init {
        val monitor = nw_path_monitor_create()
        nw_path_monitor_set_update_handler(monitor) { path ->
            isOnline = nw_path_get_status(path) == nw_path_status_satisfied
            isWifiOrWired =
                nw_path_uses_interface_type(path, nw_interface_type_wifi) ||
                nw_path_uses_interface_type(path, nw_interface_type_wired)
        }
        nw_path_monitor_set_queue(monitor, dispatch_queue_create("com.jeluchu.network.path", null))
        nw_path_monitor_start(monitor)
    }
}
