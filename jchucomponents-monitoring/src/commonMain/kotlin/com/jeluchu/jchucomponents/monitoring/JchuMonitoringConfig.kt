package com.jeluchu.jchucomponents.monitoring

import com.jeluchu.jchucomponents.config.JchuConfigBuilder
import com.jeluchu.jchucomponents.config.JchuFeature
import com.jeluchu.jchucomponents.config.JchuPlatform
import org.koin.core.module.Module
import org.koin.dsl.module

/** Options of the `crashlytics { }` block of `JchuConfig`. */
class JchuCrashlyticsBlock {
    /** Turn crash collection off (for example in debug builds). */
    var collectionEnabled: Boolean = true

    /** Custom backend. Null uses Firebase Crashlytics when the app ships it. */
    var backend: JchuCrashReporter? = null
}

/** Options of the `analytics { }` block of `JchuConfig`. */
class JchuAnalyticsBlock {
    /** Turn analytics collection off (for example in debug builds or before consent). */
    var collectionEnabled: Boolean = true

    /** Custom backend. Null uses Firebase Analytics when the app ships it. */
    var backend: JchuAnalytics? = null
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
    install(JchuCrashlyticsFeature(options.collectionEnabled, options.backend))
}

/** Enables usage statistics through [JchuMonitoring.analytics]. */
fun JchuConfigBuilder.analytics(block: JchuAnalyticsBlock.() -> Unit = {}) {
    val options = JchuAnalyticsBlock().apply(block)
    install(JchuAnalyticsFeature(options.collectionEnabled, options.backend))
}

internal class JchuCrashlyticsFeature(
    private val collectionEnabled: Boolean,
    private val backend: JchuCrashReporter?
) : JchuFeature {
    override val id: String = ID

    override fun initialize(platform: JchuPlatform) {
        val reporter = backend ?: createPlatformCrashReporter(platform) ?: NoOpCrashReporter
        JchuMonitoring.installCrash(reporter)
        JchuMonitoring.crash.setCollectionEnabled(collectionEnabled)
    }

    override fun modules(platform: JchuPlatform): List<Module> =
        listOf(module { single<JchuCrashReporter> { JchuMonitoring.crash } })

    companion object {
        const val ID = "crashlytics"
    }
}

internal class JchuAnalyticsFeature(
    private val collectionEnabled: Boolean,
    private val backend: JchuAnalytics?
) : JchuFeature {
    override val id: String = ID

    override fun initialize(platform: JchuPlatform) {
        val reporter = backend ?: createPlatformAnalytics(platform) ?: NoOpAnalytics
        JchuMonitoring.installAnalytics(reporter)
        JchuMonitoring.analytics.setCollectionEnabled(collectionEnabled)
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
