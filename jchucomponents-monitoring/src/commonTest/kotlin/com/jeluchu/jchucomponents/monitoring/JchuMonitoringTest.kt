package com.jeluchu.jchucomponents.monitoring

import com.jeluchu.jchucomponents.config.JchuConfig
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JchuMonitoringTest {
    @AfterTest
    fun cleanUp() = JchuMonitoring.reset()

    private class RecordingCrash : JchuCrashReporter {
        val calls = mutableListOf<String>()
        override fun log(message: String) { calls += "log:$message" }
        override fun recordException(throwable: Throwable, context: String?) { calls += "error:${throwable.message}:$context" }
        override fun setUserId(userId: String?) { calls += "user:$userId" }
        override fun setCustomKey(key: String, value: String) { calls += "key:$key=$value" }
        override fun setCollectionEnabled(enabled: Boolean) { calls += "enabled:$enabled" }
    }

    private class RecordingAnalytics : JchuAnalytics {
        val events = mutableListOf<Pair<String, Map<String, Any?>>>()
        var enabled: Boolean? = null
        override fun logEvent(name: String, params: Map<String, Any?>) { events += name to params }
        override fun logScreen(name: String, screenClass: String?) { events += "screen:$name" to emptyMap() }
        override fun setUserId(userId: String?) = Unit
        override fun setUserProperty(name: String, value: String?) = Unit
        override fun setCollectionEnabled(enabled: Boolean) { this.enabled = enabled }
    }

    private class Exploding : JchuCrashReporter {
        override fun log(message: String) = error("boom")
        override fun recordException(throwable: Throwable, context: String?) = error("boom")
        override fun setUserId(userId: String?) = error("boom")
        override fun setCustomKey(key: String, value: String) = error("boom")
        override fun setCollectionEnabled(enabled: Boolean) = error("boom")
    }

    @Test
    fun reportersAreNoOpsUntilConfigured() {
        JchuMonitoring.crash.recordException(IllegalStateException("x"))
        JchuMonitoring.analytics.logEvent("open")
    }

    @Test
    fun crashlyticsBlockInstallsTheBackendAndAppliesCollection() {
        val backend = RecordingCrash()
        JchuConfig { crashlytics { this.backend = backend; collectionEnabled = false } }

        JchuMonitoring.crash.recordException(IllegalStateException("bad"), "Home")
        assertEquals(listOf("enabled:false", "error:bad:Home"), backend.calls)
    }

    @Test
    fun analyticsBlockSanitizesEventsAndParameters() {
        val backend = RecordingAnalytics()
        JchuConfig { analytics { this.backend = backend } }

        JchuMonitoring.analytics.logEvent("1 add-to cart!", mapOf("item id" to 7, "ok" to true, "n" to null))

        assertEquals(true, backend.enabled)
        assertEquals("add_to_cart_", backend.events.single().first)
        assertEquals(mapOf("item_id" to 7, "ok" to true, "n" to ""), backend.events.single().second)
    }

    @Test
    fun aFailingBackendNeverPropagates() {
        JchuConfig { crashlytics { backend = Exploding() } }
        JchuMonitoring.crash.log("hello")
        JchuMonitoring.crash.recordException(IllegalStateException("x"))
    }

    @Test
    fun rulesBoundNamesAndValues() {
        assertEquals("event", JchuAnalyticsRules.eventName("123"))
        assertEquals(40, JchuAnalyticsRules.eventName("a".repeat(80)).length)
        assertTrue(JchuAnalyticsRules.params((1..40).associate { "k$it" to "v" }).size == JchuAnalyticsRules.MAX_PARAMS)
        assertEquals(100, (JchuAnalyticsRules.params(mapOf("a" to "x".repeat(300)))["a"] as String).length)
    }
}
