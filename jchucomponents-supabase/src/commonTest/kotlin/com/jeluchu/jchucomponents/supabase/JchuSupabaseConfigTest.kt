package com.jeluchu.jchucomponents.supabase

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

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
}
