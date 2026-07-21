package com.jeluchu.jchucomponents.supabase.di

import com.jeluchu.jchucomponents.supabase.JchuSupabase
import com.jeluchu.jchucomponents.supabase.JchuSupabaseConfig
import com.jeluchu.jchucomponents.supabase.createJchuSupabaseClient
import org.koin.core.module.Module
import org.koin.dsl.module

fun jchuSupabaseModule(
    config: JchuSupabaseConfig
): Module =
    jchuSupabaseModule {
        config
    }

fun jchuSupabaseModule(
    configProvider: () -> JchuSupabaseConfig
): Module {
    val config = configProvider()

    return module {
        single<JchuSupabase> {
            createJchuSupabaseClient(config)
        }
        if (config.modules.auth) single { get<JchuSupabase>().auth }
        if (config.modules.database) single { get<JchuSupabase>().database }
        if (config.modules.functions) single { get<JchuSupabase>().functions }
        if (config.modules.realtime) single { get<JchuSupabase>().realtime }
    }
}
