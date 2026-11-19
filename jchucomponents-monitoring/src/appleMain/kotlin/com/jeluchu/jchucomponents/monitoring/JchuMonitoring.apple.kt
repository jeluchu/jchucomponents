@file:OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)

package com.jeluchu.jchucomponents.monitoring

import com.jeluchu.jchucomponents.config.JchuPlatform
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import com.jeluchu.jchucomponents.monitoring.objc.jchuClassNamed
import com.jeluchu.jchucomponents.monitoring.objc.jchuResponds
import com.jeluchu.jchucomponents.monitoring.objc.jchuSendBool
import com.jeluchu.jchucomponents.monitoring.objc.jchuSendObject0
import com.jeluchu.jchucomponents.monitoring.objc.jchuSendVoid1
import com.jeluchu.jchucomponents.monitoring.objc.jchuSendVoid2
import platform.Foundation.NSError
import platform.Foundation.NSLocalizedDescriptionKey
import platform.Foundation.NSNumber
import platform.Foundation.NSSelectorFromString

/*
 * The Firebase iOS SDK is linked by the app (Swift Package Manager / CocoaPods), so Kotlin cannot reference
 * its types. Firebase exposes a stable Objective-C API, and an Objective-C class is itself an object that can
 * receive messages, so the reporters talk to it through the Objective-C runtime with typed objc_msgSend
 * wrappers (see jchuObjc.def). When the SDK is not linked objc_getClass returns null and the reporters are
 * simply not created. No Swift code is needed in the app.
 */

internal actual fun createPlatformCrashReporter(platform: JchuPlatform): JchuCrashReporter? {
    val cls = jchuClassNamed("FIRCrashlytics") ?: return null
    val instance = jchuSendObject0(cls, NSSelectorFromString("crashlytics")) ?: return null
    return FirebaseCrashlyticsObjc(instance)
}

internal actual fun createPlatformAnalytics(platform: JchuPlatform): JchuAnalytics? =
    jchuClassNamed("FIRAnalytics")?.let { FirebaseAnalyticsObjc(it) }

/** Sends a void [selector] taking up to two object arguments; ignored when the receiver does not implement it. */
internal fun Any.sendVoid(selector: String, first: Any? = null, second: Any? = null) {
    val sel = NSSelectorFromString(selector)
    if (!jchuResponds(this, sel)) return
    if (second != null) jchuSendVoid2(this, sel, first, second) else jchuSendVoid1(this, sel, first)
}

internal fun Any.sendBoolean(selector: String, value: Boolean) {
    val sel = NSSelectorFromString(selector)
    if (jchuResponds(this, sel)) jchuSendBool(this, sel, value)
}

private class FirebaseCrashlyticsObjc(private val crashlytics: Any) : JchuCrashReporter {
    override fun log(message: String) = crashlytics.sendVoid("log:", message)

    override fun recordException(throwable: Throwable, context: String?) {
        if (context != null) {
            crashlytics.sendVoid("setCustomValue:forKey:", context, "last_error_context")
            crashlytics.sendVoid("log:", "Non-fatal error in $context")
        }
        val message = throwable.message ?: throwable::class.simpleName ?: "Kotlin exception"
        val error = NSError.errorWithDomain(
            domain = throwable::class.simpleName ?: "KotlinException",
            code = 0,
            userInfo = mapOf<Any?, Any?>(
                NSLocalizedDescriptionKey to message,
                "stacktrace" to throwable.stackTraceToString().take(MAX_STACK_TRACE)
            )
        )
        crashlytics.sendVoid("recordError:", error)
    }

    override fun setUserId(userId: String?) = crashlytics.sendVoid("setUserID:", userId.orEmpty())

    override fun setCustomKey(key: String, value: String) = crashlytics.sendVoid("setCustomValue:forKey:", value, key)

    override fun setCollectionEnabled(enabled: Boolean) = crashlytics.sendBoolean("setCrashlyticsCollectionEnabled:", enabled)

    private companion object {
        const val MAX_STACK_TRACE = 4000
    }
}

private class FirebaseAnalyticsObjc(private val analytics: Any) : JchuAnalytics {
    override fun logEvent(name: String, params: Map<String, Any?>) =
        analytics.sendVoid("logEventWithName:parameters:", name, params.toObjcParams())

    override fun logScreen(name: String, screenClass: String?) {
        val params = buildMap<String, Any?> {
            put("screen_name", name.take(JchuAnalyticsRules.MAX_PARAM_VALUE))
            screenClass?.let { put("screen_class", it.take(JchuAnalyticsRules.MAX_PARAM_VALUE)) }
        }
        analytics.sendVoid("logEventWithName:parameters:", "screen_view", params.toObjcParams())
    }

    override fun setUserId(userId: String?) = analytics.sendVoid("setUserID:", userId)

    override fun setUserProperty(name: String, value: String?) = analytics.sendVoid("setUserPropertyString:forName:", value, name)

    override fun setCollectionEnabled(enabled: Boolean) = analytics.sendBoolean("setAnalyticsCollectionEnabled:", enabled)
}

private fun Map<String, Any?>.toObjcParams(): Map<Any?, Any?> =
    entries.associate { (key, value) ->
        key to when (value) {
            is Int -> NSNumber(int = value)
            is Long -> NSNumber(longLong = value)
            is Float -> NSNumber(float = value)
            is Double -> NSNumber(double = value)
            is Boolean -> NSNumber(bool = value)
            else -> value?.toString().orEmpty()
        }
    }
