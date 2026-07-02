package com.jeluchu.jchucomponents.ktx.context

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.RequiresPermission
import com.jeluchu.jchucomponents.network.android.checkNetworkState as networkCheckNetworkState
import com.jeluchu.jchucomponents.network.android.connectivityManager as networkConnectivityManager
import com.jeluchu.jchucomponents.network.android.downstreamBandwidth as networkDownstreamBandwidth
import com.jeluchu.jchucomponents.network.android.isConnectionAvailable as networkIsConnectionAvailable
import com.jeluchu.jchucomponents.network.android.isRoamingConnection as networkIsRoamingConnection

@Deprecated("Use the jchucomponents-network Android extension")
public inline val Context.connectivityManager: ConnectivityManager
    get() = networkConnectivityManager

@Deprecated("Use the jchucomponents-network Android extension")
@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
public fun Context.checkNetworkState(): Boolean = networkCheckNetworkState()

@Deprecated("Use the jchucomponents-network Android extension")
public val Context.downstreamBandwidth: Int
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    get() = networkDownstreamBandwidth

@Deprecated("Use the jchucomponents-network Android extension")
@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
public fun Context.isRoamingConnection(): Boolean = networkIsRoamingConnection()

@Deprecated("Use the jchucomponents-network Android extension")
@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
public fun Context.isConnectionAvailable(): Boolean = networkIsConnectionAvailable()
