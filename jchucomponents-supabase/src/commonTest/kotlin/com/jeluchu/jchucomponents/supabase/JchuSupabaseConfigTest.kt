package com.jeluchu.jchucomponents.supabase

import io.github.jan.supabase.auth.MemoryCodeVerifierCache
import io.github.jan.supabase.auth.MemorySessionManager
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame
import kotlin.time.Duration

class JchuSupabaseConfigTest {
    @Test
    fun configRejectsBlankCredentials() {
        assertFailsWith<IllegalArgumentException> {
            JchuSupabaseConfig(
                url = "",
                publishableKey = "publishable-key"
            )
        }
        assertFailsWith<IllegalArgumentException> {
            JchuSupabaseConfig(
                url = "https://example.supabase.co",
                publishableKey = ""
            )
        }
    }

    @Test
    fun configKeepsTheSelectedModules() {
        val modules =
            JchuSupabaseModules(
                auth = false,
                database = true,
                functions = false,
                realtime = false
            )

        val config =
            JchuSupabaseConfig(
                url = "https://example.supabase.co",
                publishableKey = "publishable-key",
                modules = modules
            )

        assertEquals(expected = modules, actual = config.modules)
    }

    @Test
    fun authConfigUsesPersistentPkceSessionDefaults() {
        val config = JchuSupabaseAuthConfig()

        assertEquals(expected = JchuSupabaseAuthFlowType.PKCE, actual = config.flowType)
        assertEquals(expected = true, actual = config.alwaysAutoRefresh)
        assertEquals(expected = true, actual = config.autoLoadFromStorage)
        assertEquals(expected = true, actual = config.autoSaveToStorage)
    }

    @Test
    fun authConfigAcceptsCustomPersistence() {
        val sessionManager = MemorySessionManager()
        val codeVerifierCache = MemoryCodeVerifierCache()

        val config =
            JchuSupabaseAuthConfig(
                sessionManager = sessionManager,
                codeVerifierCache = codeVerifierCache
            )

        assertSame(expected = sessionManager, actual = config.sessionManager)
        assertSame(expected = codeVerifierCache, actual = config.codeVerifierCache)
    }

    @Test
    fun authConfigRejectsIncompleteDeepLinkAndInvalidRetryDelay() {
        assertFailsWith<IllegalArgumentException> {
            JchuSupabaseAuthConfig(scheme = "com.example.app")
        }
        assertFailsWith<IllegalArgumentException> {
            JchuSupabaseAuthConfig(retryDelay = Duration.ZERO)
        }
    }
}
