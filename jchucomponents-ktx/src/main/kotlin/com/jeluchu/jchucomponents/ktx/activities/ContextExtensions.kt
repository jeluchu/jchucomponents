package com.jeluchu.jchucomponents.ktx.activities

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

/**
 * Finds the [Activity] wrapped by this context, if one exists.
 */
fun Context.findActivity(): Activity? {
    var current: Context = this
    while (current is ContextWrapper) {
        if (current is Activity) return current
        current = current.baseContext
    }
    return current as? Activity
}
