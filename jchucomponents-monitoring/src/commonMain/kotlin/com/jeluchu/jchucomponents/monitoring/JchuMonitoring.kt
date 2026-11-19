package com.jeluchu.jchucomponents.monitoring

/**
 * Crash and non-fatal error reporting. The default backend is Firebase Crashlytics when the app ships it,
 * otherwise nothing happens. Every call is safe: a failing backend never breaks the app.
 */
interface JchuCrashReporter {
    /** Adds a breadcrumb to the next crash report. */
    fun log(message: String)

    /** Reports a handled error. [context] says where it happened (screen, use case...). */
    fun recordException(throwable: Throwable, context: String? = null)

    fun setUserId(userId: String?)

    fun setCustomKey(key: String, value: String)

    fun setCollectionEnabled(enabled: Boolean)
}

/**
 * Usage statistics. The default backend is Firebase Analytics when the app ships it, otherwise nothing happens.
 * Event and parameter names are sanitized to Firebase's rules (see [JchuAnalyticsRules]).
 */
interface JchuAnalytics {
    /** [params] values may be String, Int, Long, Float, Double or Boolean; other values are sent as text. */
    fun logEvent(name: String, params: Map<String, Any?> = emptyMap())

    fun logScreen(name: String, screenClass: String? = null)

    fun setUserId(userId: String?)

    fun setUserProperty(name: String, value: String?)

    fun setCollectionEnabled(enabled: Boolean)
}

/**
 * Entry point used by the app code, wired by `crashlytics { }` and `analytics { }` in `JchuConfig`.
 * Until they are configured both reporters are safe no-ops, so shared code can call them unconditionally.
 */
object JchuMonitoring {
    var crash: JchuCrashReporter = NoOpCrashReporter
        private set

    var analytics: JchuAnalytics = NoOpAnalytics
        private set

    internal fun installCrash(reporter: JchuCrashReporter) {
        crash = SafeCrashReporter(reporter)
    }

    internal fun installAnalytics(reporter: JchuAnalytics) {
        analytics = SafeAnalytics(reporter)
    }

    /** Restores the no-op reporters. Meant for tests. */
    fun reset() {
        crash = NoOpCrashReporter
        analytics = NoOpAnalytics
    }
}

internal object NoOpCrashReporter : JchuCrashReporter {
    override fun log(message: String) = Unit
    override fun recordException(throwable: Throwable, context: String?) = Unit
    override fun setUserId(userId: String?) = Unit
    override fun setCustomKey(key: String, value: String) = Unit
    override fun setCollectionEnabled(enabled: Boolean) = Unit
}

internal object NoOpAnalytics : JchuAnalytics {
    override fun logEvent(name: String, params: Map<String, Any?>) = Unit
    override fun logScreen(name: String, screenClass: String?) = Unit
    override fun setUserId(userId: String?) = Unit
    override fun setUserProperty(name: String, value: String?) = Unit
    override fun setCollectionEnabled(enabled: Boolean) = Unit
}

private class SafeCrashReporter(private val delegate: JchuCrashReporter) : JchuCrashReporter {
    override fun log(message: String) = guard { delegate.log(message) }
    override fun recordException(throwable: Throwable, context: String?) = guard { delegate.recordException(throwable, context) }
    override fun setUserId(userId: String?) = guard { delegate.setUserId(userId) }
    override fun setCustomKey(key: String, value: String) = guard { delegate.setCustomKey(key, value) }
    override fun setCollectionEnabled(enabled: Boolean) = guard { delegate.setCollectionEnabled(enabled) }
}

private class SafeAnalytics(private val delegate: JchuAnalytics) : JchuAnalytics {
    override fun logEvent(name: String, params: Map<String, Any?>) = guard {
        delegate.logEvent(JchuAnalyticsRules.eventName(name), JchuAnalyticsRules.params(params))
    }

    override fun logScreen(name: String, screenClass: String?) = guard { delegate.logScreen(name, screenClass) }
    override fun setUserId(userId: String?) = guard { delegate.setUserId(userId) }
    override fun setUserProperty(name: String, value: String?) = guard {
        delegate.setUserProperty(JchuAnalyticsRules.propertyName(name), value?.take(JchuAnalyticsRules.MAX_PROPERTY_VALUE))
    }

    override fun setCollectionEnabled(enabled: Boolean) = guard { delegate.setCollectionEnabled(enabled) }
}

private inline fun guard(block: () -> Unit) {
    try {
        block()
    } catch (_: Throwable) {
        // Monitoring must never take the app down.
    }
}
