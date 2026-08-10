package com.jeluchu.jchucomponents.supabase

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.FlowType
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.functions.Functions
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime

fun createJchuSupabaseClient(config: JchuSupabaseConfig): JchuSupabase =
    JchuSupabase(
        client =
            createSupabaseClient(
                supabaseUrl = config.url,
                supabaseKey = config.publishableKey
            ) {
                if (config.modules.auth) {
                    install(Auth) {
                        alwaysAutoRefresh = config.auth.alwaysAutoRefresh
                        autoLoadFromStorage = config.auth.autoLoadFromStorage
                        autoSaveToStorage = config.auth.autoSaveToStorage
                        retryDelay = config.auth.retryDelay
                        flowType =
                            when (config.auth.flowType) {
                                JchuSupabaseAuthFlowType.PKCE -> FlowType.PKCE
                                JchuSupabaseAuthFlowType.IMPLICIT -> FlowType.IMPLICIT
                            }
                        config.auth.sessionManager?.let { sessionManager = it }
                        config.auth.codeVerifierCache?.let { codeVerifierCache = it }
                        enableLifecycleCallbacks = config.auth.enableLifecycleCallbacks
                        config.auth.defaultRedirectUrl?.let { defaultRedirectUrl = it }
                        config.auth.scheme?.let { scheme = it }
                        config.auth.host?.let { host = it }
                    }
                }
                if (config.modules.database) install(Postgrest)
                if (config.modules.functions) install(Functions)
                if (config.modules.realtime) install(Realtime)
            },
        modules = config.modules
    )
