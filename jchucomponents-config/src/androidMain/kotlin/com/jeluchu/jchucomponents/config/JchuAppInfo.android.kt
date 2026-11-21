package com.jeluchu.jchucomponents.config

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build

actual object JchuAppInfo {
    @Volatile
    private var context: Context? = null

    /** Attaches the application [context]. `JchuConfig(application)` does it; call it only to read this earlier. */
    fun init(context: Context) {
        this.context = context.applicationContext ?: context
    }

    actual val platform: String = "android"

    actual val isDebug: Boolean
        get() {
            val appInfo = applicationInfo() ?: return false
            val flavor = appInfo.metaData?.get("app.flavor")?.toString()
            return flavor?.let { it == "debug" } ?: (appInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0)
        }

    actual val versionName: String
        get() = packageInfo()?.versionName ?: UNKNOWN

    actual val versionCode: String
        get() =
            packageInfo()?.let { info ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    info.longVersionCode.toString()
                } else {
                    @Suppress("DEPRECATION")
                    info.versionCode.toString()
                }
            } ?: UNKNOWN

    actual fun metadata(key: String): String = applicationInfo()?.metaData?.get(key)?.toString().orEmpty()

    private fun packageInfo() =
        context?.let { ctx -> runCatching { ctx.packageManager.getPackageInfo(ctx.packageName, 0) }.getOrNull() }

    private fun applicationInfo() =
        context?.let { ctx ->
            runCatching {
                ctx.packageManager.getApplicationInfo(ctx.packageName, PackageManager.GET_META_DATA)
            }.getOrNull()
        }

    private const val UNKNOWN = "Unknown"
}

internal actual fun attachJchuAppInfo(platform: JchuPlatform) {
    (platform as? JchuAndroidPlatform)?.let { JchuAppInfo.init(it.context) }
}
