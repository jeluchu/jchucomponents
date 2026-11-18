package com.jeluchu.jchucomponents.supabase.di

import com.jeluchu.jchucomponents.config.JchuConfig
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class JchuSupabaseConfigBlockTest {
    @Test
    fun supabaseBlockContributesTheSupabaseModule() {
        val result =
            JchuConfig {
                supabase {
                    url = "https://project.supabase.co"
                    publishableKey = "sb_publishable_key"
                }
            }

        assertEquals(1, result.modules.size)
    }

    @Test
    fun missingUrlIsRejected() {
        assertFailsWith<IllegalArgumentException> {
            JchuConfig { supabase { publishableKey = "sb_publishable_key" } }
        }
    }
}
