/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ktx.context.handler

import android.content.Context
import com.jeluchu.jchucomponents.ktx.context.checkNetworkState

class NetworkHandler
    (private val context: Context) {
    val isConnected get() = context.checkNetworkState()
}