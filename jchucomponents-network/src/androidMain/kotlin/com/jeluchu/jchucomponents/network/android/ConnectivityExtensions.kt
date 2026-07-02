package com.jeluchu.jchucomponents.network.android

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresPermission

public inline val Context.connectivityManager: ConnectivityManager
    get() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

@Suppress("DEPRECATION")
@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
public fun Context.checkNetworkState(): Boolean {
    val manager = connectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = manager.activeNetwork ?: return false
        val capabilities = manager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }
    return manager.activeNetworkInfo?.isConnected == true
}

public val Context.downstreamBandwidth: Int
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    get() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return 0
        val network = connectivityManager.activeNetwork ?: return 0
        return connectivityManager
            .getNetworkCapabilities(network)
            ?.linkDownstreamBandwidthKbps
            ?: 0
    }

@Suppress("DEPRECATION")
@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
public fun Context.isRoamingConnection(): Boolean {
    val manager = connectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val network = manager.activeNetwork ?: return false
        val capabilities = manager.getNetworkCapabilities(network) ?: return false
        return !capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_ROAMING)
    }
    return manager.activeNetworkInfo?.isRoaming == true
}

@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
public fun Context.isConnectionAvailable(): Boolean = checkNetworkState()
