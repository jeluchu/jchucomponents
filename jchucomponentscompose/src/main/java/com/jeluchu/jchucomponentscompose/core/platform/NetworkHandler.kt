package com.jeluchu.jchucomponentscompose.core.platform

import android.content.Context
import android.net.NetworkCapabilities
import android.os.Build
import com.jeluchu.jchucomponentscompose.core.extensions.context.checkNetworkState

class NetworkHandler
    (private val context: Context) {
    val isConnected get() = context.checkNetworkState()
}