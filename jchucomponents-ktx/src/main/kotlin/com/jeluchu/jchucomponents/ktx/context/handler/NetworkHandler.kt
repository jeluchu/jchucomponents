/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ktx.context.handler

import android.content.Context
import com.jeluchu.jchucomponents.ktx.context.checkNetworkState as legacyCheckNetworkState

class NetworkHandler(private val context: Context) {
    @Suppress("DEPRECATION")
    val isConnected: Boolean
        get() = context.legacyCheckNetworkState()
}
