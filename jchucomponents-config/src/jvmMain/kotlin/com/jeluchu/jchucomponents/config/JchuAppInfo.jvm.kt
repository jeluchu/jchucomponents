package com.jeluchu.jchucomponents.config

actual object JchuAppInfo {
    actual val platform: String = "jvm"

    actual val isDebug: Boolean
        get() = System.getProperty("jchu.debug")?.toBoolean() ?: false

    actual val versionName: String
        get() = System.getProperty("app.version") ?: UNKNOWN

    actual val versionCode: String
        get() = System.getProperty("app.build") ?: UNKNOWN

    actual fun metadata(key: String): String = System.getProperty(key).orEmpty()

    private const val UNKNOWN = "Unknown"
}

internal actual fun attachJchuAppInfo(platform: JchuPlatform) = Unit
