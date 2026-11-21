package com.jeluchu.jchucomponents.config

import platform.Foundation.NSBundle
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.OsFamily
import kotlin.native.Platform

@OptIn(ExperimentalNativeApi::class)
actual object JchuAppInfo {
    actual val platform: String
        get() = if (Platform.osFamily == OsFamily.MACOSX) "macos" else "ios"

    actual val isDebug: Boolean
        get() = infoValue("CFBundleConfiguration")?.let { it == "debug" } ?: Platform.isDebugBinary

    actual val versionName: String
        get() = infoValue("CFBundleShortVersionString") ?: UNKNOWN

    actual val versionCode: String
        get() = infoValue("CFBundleVersion") ?: UNKNOWN

    actual fun metadata(key: String): String = infoValue(key).orEmpty()

    private fun infoValue(key: String): String? = NSBundle.mainBundle.objectForInfoDictionaryKey(key) as? String

    private const val UNKNOWN = "Unknown"
}

internal actual fun attachJchuAppInfo(platform: JchuPlatform) = Unit
