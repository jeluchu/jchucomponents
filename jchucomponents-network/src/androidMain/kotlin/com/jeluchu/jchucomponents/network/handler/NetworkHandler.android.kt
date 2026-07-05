package com.jeluchu.jchucomponents.network.handler

import android.Manifest
import android.content.Context
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresPermission
import com.jeluchu.jchucomponents.network.extensions.connectivityManager

actual class NetworkHandler(
    private val context: Context
) {
    @Suppress("DEPRECATION")
    @RequiresPermission(value = Manifest.permission.ACCESS_NETWORK_STATE)
    actual fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.connectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)
        }

        return connectivityManager.activeNetworkInfo?.isConnected == true
    }
}
