package com.jeluchu.jchucomponents.monitoring

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.jeluchu.jchucomponents.config.JchuPlatform
import com.jeluchu.jchucomponents.config.androidContext

// Firebase is compile-only: when the app does not ship it, creating the reporter throws and we return null.
internal actual fun createPlatformCrashReporter(platform: JchuPlatform): JchuCrashReporter? =
    runCatching { FirebaseCrashlyticsReporter(FirebaseCrashlytics.getInstance()) }.getOrNull()

internal actual fun createPlatformAnalytics(platform: JchuPlatform): JchuAnalytics? =
    runCatching { FirebaseAnalyticsReporter(FirebaseAnalytics.getInstance(platform.androidContext)) }.getOrNull()

private class FirebaseCrashlyticsReporter(private val crashlytics: FirebaseCrashlytics) : JchuCrashReporter {
    override fun log(message: String) = crashlytics.log(message)

    override fun recordException(throwable: Throwable, context: String?) {
        if (context != null) {
            crashlytics.setCustomKey("last_error_context", context)
            crashlytics.log("Non-fatal error in $context")
        }
        crashlytics.recordException(throwable)
    }

    override fun setUserId(userId: String?) = crashlytics.setUserId(userId.orEmpty())
    override fun setCustomKey(key: String, value: String) = crashlytics.setCustomKey(key, value)
    override fun setCollectionEnabled(enabled: Boolean) = crashlytics.setCrashlyticsCollectionEnabled(enabled)
}

private class FirebaseAnalyticsReporter(private val analytics: FirebaseAnalytics) : JchuAnalytics {
    override fun logEvent(name: String, params: Map<String, Any?>) = analytics.logEvent(name, params.toBundle())

    override fun logScreen(name: String, screenClass: String?) {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, name.take(JchuAnalyticsRules.MAX_PARAM_VALUE))
            screenClass?.let { putString(FirebaseAnalytics.Param.SCREEN_CLASS, it.take(JchuAnalyticsRules.MAX_PARAM_VALUE)) }
        }
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    override fun setUserId(userId: String?) = analytics.setUserId(userId)
    override fun setUserProperty(name: String, value: String?) = analytics.setUserProperty(name, value)
    override fun setCollectionEnabled(enabled: Boolean) = analytics.setAnalyticsCollectionEnabled(enabled)
}

private fun Map<String, Any?>.toBundle(): Bundle =
    Bundle().also { bundle ->
        forEach { (key, value) ->
            when (value) {
                is Int -> bundle.putLong(key, value.toLong())
                is Long -> bundle.putLong(key, value)
                is Float -> bundle.putDouble(key, value.toDouble())
                is Double -> bundle.putDouble(key, value)
                is Boolean -> bundle.putLong(key, if (value) 1L else 0L)
                else -> bundle.putString(key, value?.toString().orEmpty())
            }
        }
    }
