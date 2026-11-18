package com.jeluchu.jchucomponents.supabase.di

import com.jeluchu.jchucomponents.supabase.JchuSupabase
import com.jeluchu.jchucomponents.supabase.JchuSupabaseConfig
import com.jeluchu.jchucomponents.supabase.createJchuSupabaseClient
import org.koin.core.module.Module
import org.koin.dsl.module

fun jchuSupabaseModule(
    config: JchuSupabaseConfig,
    createdAtStart: Boolean = false
): Module =
    jchuSupabaseModule(createdAtStart) {
        config
    }

/**
 * @param createdAtStart creates the client when Koin starts instead of on first use, so the stored
 * session starts restoring at app launch.
 */
fun jchuSupabaseModule(
    createdAtStart: Boolean = false,
    configProvider: () -> JchuSupabaseConfig
): Module {
    val config = configProvider()

    return module {
        single<JchuSupabase>(createdAtStart = createdAtStart) {
            createJchuSupabaseClient(config)
        }
        if (config.modules.auth) single { get<JchuSupabase>().auth }
        if (config.modules.database) single { get<JchuSupabase>().database }
        if (config.modules.functions) single { get<JchuSupabase>().functions }
        if (config.modules.realtime) single { get<JchuSupabase>().realtime }
    }
}
