package com.jeluchu.jchucomponents.monitoring

import com.jeluchu.jchucomponents.config.JchuAppInfo
import com.jeluchu.jchucomponents.config.JchuConfigBuilder
import com.jeluchu.jchucomponents.config.JchuFeature
import com.jeluchu.jchucomponents.config.JchuPlatform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.core.module.Module
import org.koin.dsl.module

/** Options of the `crashlytics { }` block of `JchuConfig`. */
class JchuCrashlyticsBlock {
    /** Turn crash collection off (for example in debug builds). */
    var collectionEnabled: Boolean = true

    /** Custom backend. Null uses Firebase Crashlytics when the app ships it. */
    var backend: JchuCrashReporter? = null

    /**
     * Tag every report with `app_platform`, `app_version` and `app_build` (from [JchuAppInfo]) so reports can be
     * filtered by them in the console. On by default.
     */
    var appContext: Boolean = true

    internal var userIdSource: (() -> Flow<String?>)? = null

    /**
     * Keeps the crash user id equal to the latest value of the flow [source] returns: set on login, cleared on
     * logout (null or blank). [source] runs once, right after Koin starts, so it can read what other blocks
     * registered:
     *
     * ```
     * crashlytics { userId { jchuPreferencesHelper().observeString("user_id") } }
     * ```
     */
    fun userId(source: () -> Flow<String?>) {
        userIdSource = source
    }
}

/** Options of the `analytics { }` block of `JchuConfig`. */
class JchuAnalyticsBlock {
    /** Turn analytics collection off (for example in debug builds or before consent). */
    var collectionEnabled: Boolean = true

    /** Custom backend. Null uses Firebase Analytics when the app ships it. */
    var backend: JchuAnalytics? = null

    internal var userIdSource: (() -> Flow<String?>)? = null

    /** Same as `crashlytics { userId { } }`, for the analytics user id. */
    fun userId(source: () -> Flow<String?>) {
        userIdSource = source
    }
}

/**
 * Enables crash reporting through [JchuMonitoring.crash].
 *
 * ```
 * JchuConfig(application) {
 *     crashlytics { collectionEnabled = !BuildConfig.DEBUG }
 * }
 * ```
 */
fun JchuConfigBuilder.crashlytics(block: JchuCrashlyticsBlock.() -> Unit = {}) {
    val options = JchuCrashlyticsBlock().apply(block)
    install(JchuCrashlyticsFeature(options.collectionEnabled, options.backend, options.appContext, options.userIdSource))
}

/** Enables usage statistics through [JchuMonitoring.analytics]. */
fun JchuConfigBuilder.analytics(block: JchuAnalyticsBlock.() -> Unit = {}) {
    val options = JchuAnalyticsBlock().apply(block)
    install(JchuAnalyticsFeature(options.collectionEnabled, options.backend, options.userIdSource))
}

internal class JchuCrashlyticsFeature(
    private val collectionEnabled: Boolean,
    private val backend: JchuCrashReporter?,
    private val appContext: Boolean = false,
    private val userIdSource: (() -> Flow<String?>)? = null
) : JchuFeature {
    override val id: String = ID

    override fun initialize(platform: JchuPlatform) {
        val reporter = backend ?: createPlatformCrashReporter(platform) ?: NoOpCrashReporter
        JchuMonitoring.installCrash(reporter)
        JchuMonitoring.crash.setCollectionEnabled(collectionEnabled)
        if (appContext) {
            JchuMonitoring.crash.setCustomKey("app_platform", JchuAppInfo.platform)
            JchuMonitoring.crash.setCustomKey("app_version", JchuAppInfo.versionName)
            JchuMonitoring.crash.setCustomKey("app_build", JchuAppInfo.versionCode)
        }
    }

    override fun onStarted(platform: JchuPlatform, koin: org.koin.core.Koin?) {
        followUserId(userIdSource) { JchuMonitoring.crash.setUserId(it) }
    }

    override fun modules(platform: JchuPlatform): List<Module> =
        listOf(module { single<JchuCrashReporter> { JchuMonitoring.crash } })

    companion object {
        const val ID = "crashlytics"
    }
}

internal class JchuAnalyticsFeature(
    private val collectionEnabled: Boolean,
    private val backend: JchuAnalytics?,
    private val userIdSource: (() -> Flow<String?>)? = null
) : JchuFeature {
    override val id: String = ID

    override fun initialize(platform: JchuPlatform) {
        val reporter = backend ?: createPlatformAnalytics(platform) ?: NoOpAnalytics
        JchuMonitoring.installAnalytics(reporter)
        JchuMonitoring.analytics.setCollectionEnabled(collectionEnabled)
    }

    override fun onStarted(platform: JchuPlatform, koin: org.koin.core.Koin?) {
        followUserId(userIdSource) { JchuMonitoring.analytics.setUserId(it) }
    }

    override fun modules(platform: JchuPlatform): List<Module> =
        listOf(module { single<JchuAnalytics> { JchuMonitoring.analytics } })

    companion object {
        const val ID = "analytics"
    }
}

/** Firebase Crashlytics when available on this platform, else null. */
internal expect fun createPlatformCrashReporter(platform: JchuPlatform): JchuCrashReporter?

/** Firebase Analytics when available on this platform, else null. */
internal expect fun createPlatformAnalytics(platform: JchuPlatform): JchuAnalytics?

/**
 * Collects the flow of [source] and passes each value to [apply] (blank or null becomes null). Unconfined, so
 * values that are already available apply immediately and the rest as soon as they are emitted.
 */
private fun followUserId(
    source: (() -> Flow<String?>)?,
    apply: (String?) -> Unit
) {
    val flow = source?.invoke() ?: return
    CoroutineScope(SupervisorJob() + Dispatchers.Unconfined).launch {
        flow.collect { id -> runCatching { apply(id?.takeIf { it.isNotBlank() }) } }
    }
}
