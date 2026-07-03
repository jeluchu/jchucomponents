package com.jeluchu.pay.revenuecat.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

internal fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
