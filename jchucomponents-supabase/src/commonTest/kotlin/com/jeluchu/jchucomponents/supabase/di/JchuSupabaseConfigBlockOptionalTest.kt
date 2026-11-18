package com.jeluchu.jchucomponents.supabase.di

import com.jeluchu.jchucomponents.config.JchuConfig
import kotlin.io.encoding.Base64
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class JchuSupabaseConfigBlockOptionalTest {
    private fun serviceRoleJwt(): String {
        val encode = { text: String -> Base64.UrlSafe.withPadding(Base64.PaddingOption.ABSENT).encode(text.encodeToByteArray()) }
        return "${encode("""{"alg":"HS256"}""")}.${encode("""{"role":"service_role"}""")}.signature"
    }

    private fun modulesFor(
        url: String,
        key: String,
        optional: Boolean
    ) = JchuConfig {
        supabase {
            this.url = url
            publishableKey = key
            this.optional = optional
        }
    }.modules

    @Test
    fun optionalSkipsEveryUnusableConfiguration() {
        listOf(
            "" to "",
            "https://p.supabase.co" to "YOUR_PUBLISHABLE_KEY",
            "https://p.supabase.co" to "sb_secret_abc",
            "https://p.supabase.co" to serviceRoleJwt(),
            "http://p.supabase.co" to "sb_publishable_abc"
        ).forEach { (url, key) -> assertEquals(0, modulesFor(url, key, optional = true).size, "$url / $key") }
    }

    @Test
    fun nonOptionalThrowsWithTheReason() {
        val error =
            assertFailsWith<IllegalArgumentException> {
                modulesFor("https://p.supabase.co", "sb_secret_abc", optional = false)
            }
        assertTrue("secret key" in error.message.orEmpty())
    }

    @Test
    fun usableConfigurationIsInstalledEvenWhenOptional() {
        assertEquals(1, modulesFor(" https://p.supabase.co ", " sb_publishable_abc ", optional = true).size)
        assertEquals(1, modulesFor("http://localhost:54321", "sb_publishable_abc", optional = true).size)
    }

    @Test
    fun clientIsUnavailableWithoutKoin() {
        assertNull(jchuSupabaseOrNull())
    }
}
