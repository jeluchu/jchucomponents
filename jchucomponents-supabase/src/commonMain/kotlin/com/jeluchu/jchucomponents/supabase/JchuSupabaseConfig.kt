package com.jeluchu.jchucomponents.supabase

import io.github.jan.supabase.auth.CodeVerifierCache
import io.github.jan.supabase.auth.SessionManager
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class JchuSupabaseConfig(
    val url: String,
    val publishableKey: String,
    val auth: JchuSupabaseAuthConfig = JchuSupabaseAuthConfig(),
    val modules: JchuSupabaseModules = JchuSupabaseModules()
) {
    init {
        require(url.isNotBlank()) { "Supabase url cannot be blank." }
        require(publishableKey.isNotBlank()) { "Supabase publishable key cannot be blank." }
    }
}

data class JchuSupabaseAuthConfig(
    val alwaysAutoRefresh: Boolean = true,
    val autoLoadFromStorage: Boolean = true,
    val autoSaveToStorage: Boolean = true,
    val retryDelay: Duration = 10.seconds,
    val flowType: JchuSupabaseAuthFlowType = JchuSupabaseAuthFlowType.PKCE,
    val sessionManager: SessionManager? = null,
    val codeVerifierCache: CodeVerifierCache? = null,
    val enableLifecycleCallbacks: Boolean = true,
    val defaultRedirectUrl: String? = null,
    val scheme: String? = null,
    val host: String? = null
) {
    init {
        require(retryDelay.isPositive()) { "Supabase auth retry delay must be positive." }
        require(scheme.isNullOrBlank() == host.isNullOrBlank()) {
            "Supabase auth scheme and host must be configured together."
        }
        require(scheme == null || scheme.isNotBlank()) { "Supabase auth scheme cannot be blank." }
        require(host == null || host.isNotBlank()) { "Supabase auth host cannot be blank." }
        require(defaultRedirectUrl == null || defaultRedirectUrl.isNotBlank()) {
            "Supabase auth redirect URL cannot be blank."
        }
    }
}

enum class JchuSupabaseAuthFlowType {
    PKCE,
    IMPLICIT
}

data class JchuSupabaseModules(
    val auth: Boolean = true,
    val database: Boolean = true,
    val functions: Boolean = true,
    val realtime: Boolean = true
)
