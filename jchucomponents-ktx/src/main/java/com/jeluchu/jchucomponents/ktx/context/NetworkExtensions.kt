/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ktx.context

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import com.jeluchu.jchucomponents.ktx.numbers.empty
import com.jeluchu.jchucomponents.ktx.numbers.orEmpty
import com.jeluchu.jchucomponents.ktx.packageutils.buildIsMAndLower
import com.jeluchu.jchucomponents.ktx.packageutils.buildIsMarshmallowAndUp
import com.jeluchu.jchucomponents.ktx.packageutils.buildIsPAndUp

/**
 *
 * [Context] Extension to obtain the necessary information
 * from [ConnectivityManager]
 *
 * @see android.net.ConnectivityManager
 *
 */
inline val Context.connectivityManager: ConnectivityManager
    get() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

/**
 *
 * [Context] Extension to check the internet connection status,
 * this will be checked in the usual way in addition to checking
 * if internet is available in case Wi-Fi is being used or mobile
 * data is being used, depending on the connection status a [Boolean]
 * will be returned
 *
 * The ACCESS_NETWORK_STATE permission must be included in the [Manifest]
 * for this function to be performed correctly
 *
 * @see android.net.ConnectivityManager
 * @see android.Manifest.permission.ACCESS_NETWORK_STATE
 *
 */
@Suppress("DEPRECATION")
@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.checkNetworkState(): Boolean {

    if (!buildIsMAndLower) {
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    } else {
        val nw = connectivityManager.activeNetworkInfo ?: return false
        return when (nw.type) {
            (NetworkCapabilities.TRANSPORT_WIFI) -> true
            (NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

}

/**
 *
 * [Context] Extension to check the downstream bandwidth that
 * the user has with his Internet connection, this will return an [Int]
 * with the amount of downstream bandwidth
 *
 * The ACCESS_NETWORK_STATE permission must be included in the [Context.connectivityManager]
 * for this function to be performed correctly
 *
 * @see com.jeluchu.jchucomponents.core.extensions.context.connectivityManager
 * @see android.Manifest.permission.ACCESS_NETWORK_STATE
 *
 */
val Context.downstreamBandwidth: Int
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    get() = if (buildIsMarshmallowAndUp) {
        with(connectivityManager.activeNetwork) {
            if (this != null)
                connectivityManager.getNetworkCapabilities(this)?.linkDownstreamBandwidthKbps.orEmpty()
            else Int.empty()
        }
    } else Int.empty()

/**
 *
 * [Context] Extension to check if Internet roaming is enabled
 * inside the user's device, this will return a [Boolean]
 *
 * The ACCESS_NETWORK_STATE permission must be included in the [Context.connectivityManager]
 * for this function to be performed correctly
 *
 * @see com.jeluchu.jchucomponents.core.extensions.context.connectivityManager
 * @see android.Manifest.permission.ACCESS_NETWORK_STATE
 * @see android.net.NetworkCapabilities.NET_CAPABILITY_NOT_ROAMING
 *
 */
@Suppress("DEPRECATION")
@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.isRoamingConnection(): Boolean {

    var result = false

    if (buildIsPAndUp) {
        val actNw = connectivityManager.getNetworkCapabilities(
            connectivityManager.activeNetwork ?: return false
        ) ?: return false
        result = !actNw.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_ROAMING)
    } else {
        connectivityManager.run {
            return@run connectivityManager.activeNetworkInfo?.run {
                return this.isRoaming
            }
        }
    }

    return result

}

/**
 *
 * [Context] Extension to know if the Internet
 * connection is available, this will return a [Boolean]
 *
 * The ACCESS_NETWORK_STATE permission must be included in the [Context.connectivityManager]
 * for this function to be performed correctly
 *
 * @see com.jeluchu.jchucomponents.core.extensions.context.connectivityManager
 * @see android.Manifest.permission.ACCESS_NETWORK_STATE
 * @see android.net.NetworkCapabilities.NET_CAPABILITY_NOT_ROAMING
 *
 */
@Suppress("DEPRECATION")
@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.isConnectionAvailable(): Boolean {

    var isAvailable = false
    with(connectivityManager) {

        if (buildIsMarshmallowAndUp)
            activeNetworkInfo?.let { networkInfo ->
                isAvailable = when (networkInfo.type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        else {
            val capabilities = getNetworkCapabilities(activeNetwork)
            if (capabilities != null) {
                isAvailable = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            } else isAvailable = false
        }

    }

    return isAvailable

}
