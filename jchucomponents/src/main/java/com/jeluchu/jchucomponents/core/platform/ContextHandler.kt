/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.core.platform

import android.content.Context

class ContextHandler
    (private val context: Context) {
    val appContext: Context get() = context.applicationContext
}