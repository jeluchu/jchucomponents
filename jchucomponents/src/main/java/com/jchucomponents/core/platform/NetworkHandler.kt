/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jchucomponents.core.platform

import android.content.Context
import com.jchucomponents.core.extensions.context.checkNetworkState

class NetworkHandler
    (private val context: Context) {
    val isConnected get() = context.checkNetworkState()
}