package com.jeluchu.jchucomponents.config

/**
 * Facts about the running app that every app used to read with its own `SystemProvider`.
 *
 * `JchuConfig` attaches it before running its block, so it can be read inside the block (and anywhere
 * afterwards) with no `init` call:
 *
 * ```
 * JchuConfig(application) {
 *     payments { apiKey = JchuAppInfo.metadata("revenuecat") }
 * }
 * ```
 *
 * Android reads the manifest `<meta-data>`; iOS and macOS read `Info.plist`; JVM reads system properties.
 * Nothing here throws: a missing value gives its documented default.
 */
expect object JchuAppInfo {
    /** `"android"`, `"ios"`, `"macos"` or `"jvm"`. */
    val platform: String

    /**
     * True for debug builds. Android: manifest meta-data `app.flavor` equals `debug` (the value apps already
     * inject per build type), otherwise the debuggable flag. Apple: `Info.plist` key `CFBundleConfiguration`
     * equals `debug`, otherwise a debug binary. JVM: system property `jchu.debug`.
     */
    val isDebug: Boolean

    /** Human readable version (`versionName` / `CFBundleShortVersionString`), or `"Unknown"`. */
    val versionName: String

    /** Build number (`versionCode` / `CFBundleVersion`) as text, or `"Unknown"`. */
    val versionCode: String

    /**
     * A custom value of the app: manifest `<meta-data android:name="key">` on Android, the `Info.plist` entry
     * `key` on Apple, the system property `key` on JVM. Empty when it is missing or not text.
     */
    fun metadata(key: String): String
}
