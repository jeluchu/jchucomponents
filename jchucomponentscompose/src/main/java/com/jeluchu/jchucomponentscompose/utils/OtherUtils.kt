package com.jeluchu.jchucomponentscompose.utils

import android.content.pm.PackageManager
import java.util.*

fun getRandomUUID(): Long = UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE

fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean =
    runCatching {
        packageManager.getPackageInfo(packageName, 0)
        true
    }.getOrElse {
        false
    }