package com.jeluchu.jchucomponents.ktx.utilities

object ModelHelper {
    fun getModel(): String = android.os.Build.BRAND + ' ' + android.os.Build.MODEL + ' ' + android.os.Build.DEVICE
    fun getOSVersion(): String = android.os.Build.VERSION.RELEASE
}