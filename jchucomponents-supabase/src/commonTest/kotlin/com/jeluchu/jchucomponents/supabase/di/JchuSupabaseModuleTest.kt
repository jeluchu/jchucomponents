package com.jeluchu.jchucomponents.supabase.di

import com.jeluchu.jchucomponents.supabase.JchuSupabaseAuthConfig
import com.jeluchu.jchucomponents.supabase.JchuSupabaseConfig
import com.jeluchu.jchucomponents.supabase.JchuSupabaseModules
import com.jeluchu.jchucomponents.supabase.auth.JchuSupabaseAuth
import com.jeluchu.jchucomponents.supabase.database.JchuSupabaseDatabase
import com.jeluchu.jchucomponents.supabase.functions.JchuSupabaseFunctions
import com.jeluchu.jchucomponents.supabase.realtime.JchuSupabaseRealtime
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertNull

class JchuSupabaseModuleTest {
    @AfterTest
    fun stopKoinAfterTest() {
        stopKoin()
    }

    @Test
    fun disabledServicesAreNotRegistered() {
        val koin =
            startKoin {
                modules(
                    jchuSupabaseModule(
                        JchuSupabaseConfig(
                            url = "https://example.supabase.co",
                            publishableKey = "publishable-key",
                            auth = JchuSupabaseAuthConfig(autoLoadFromStorage = false),
                            modules =
                                JchuSupabaseModules(
                                    auth = false,
                                    database = false,
                                    functions = false,
                                    realtime = false
                                )
                        )
                    )
                )
            }.koin

        assertNull(koin.getOrNull<JchuSupabaseAuth>())
        assertNull(koin.getOrNull<JchuSupabaseDatabase>())
        assertNull(koin.getOrNull<JchuSupabaseFunctions>())
        assertNull(koin.getOrNull<JchuSupabaseRealtime>())
    }
}
