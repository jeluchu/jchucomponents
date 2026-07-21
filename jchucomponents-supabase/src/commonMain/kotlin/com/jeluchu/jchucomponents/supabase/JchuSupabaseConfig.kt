package com.jeluchu.jchucomponents.supabase

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
    val autoLoadFromStorage: Boolean = true,
    val autoSaveToStorage: Boolean = true,
    val defaultRedirectUrl: String? = null,
    val scheme: String? = null,
    val host: String? = null
)

data class JchuSupabaseModules(
    val auth: Boolean = true,
    val database: Boolean = true,
    val functions: Boolean = true,
    val realtime: Boolean = true
)
