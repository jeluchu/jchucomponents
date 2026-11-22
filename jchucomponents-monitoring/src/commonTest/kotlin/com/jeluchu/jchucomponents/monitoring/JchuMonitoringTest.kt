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
        JchuConfig { crashlytics { this.backend = backend; collectionEnabled = false; appContext = false } }

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

    @Test
    fun crashlyticsTagsReportsWithTheAppContextByDefault() {
        val crash = RecordingCrash()

        JchuConfig { crashlytics { backend = crash } }

        val keys = crash.calls.filter { it.startsWith("key:") }.map { it.substringAfter("key:").substringBefore("=") }
        assertEquals(listOf("app_platform", "app_version", "app_build"), keys)
    }

    @Test
    fun theAppContextCanBeTurnedOff() {
        val crash = RecordingCrash()

        JchuConfig { crashlytics { backend = crash; appContext = false } }

        assertTrue(crash.calls.none { it.startsWith("key:") })
    }

    @Test
    fun theCrashUserIdFollowsTheFlowAndClearsOnBlank() {
        val crash = RecordingCrash()
        val user = kotlinx.coroutines.flow.MutableStateFlow<String?>(null)

        JchuConfig {
            crashlytics {
                backend = crash
                appContext = false
                userId { user }
            }
        }
        user.value = "u-1"
        user.value = "  "
        user.value = "u-2"

        assertEquals(listOf("user:null", "user:u-1", "user:null", "user:u-2"), crash.calls.filter { it.startsWith("user:") })
    }

    @Test
    fun theAnalyticsUserIdFollowsTheFlowToo() {
        val analytics = object : JchuAnalytics {
            val ids = mutableListOf<String?>()
            override fun logEvent(name: String, params: Map<String, Any?>) = Unit
            override fun logScreen(name: String, screenClass: String?) = Unit
            override fun setUserId(userId: String?) { ids += userId }
            override fun setUserProperty(name: String, value: String?) = Unit
            override fun setCollectionEnabled(enabled: Boolean) = Unit
        }
        val user = kotlinx.coroutines.flow.MutableStateFlow<String?>("a")

        JchuConfig { analytics { backend = analytics; userId { user } } }
        user.value = "b"

        assertEquals(listOf<String?>("a", "b"), analytics.ids)
    }
}
