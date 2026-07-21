package com.jeluchu.jchucomponents.supabase

import com.jeluchu.jchucomponents.supabase.auth.JchuSupabaseAuth
import com.jeluchu.jchucomponents.supabase.database.JchuSupabaseDatabase
import com.jeluchu.jchucomponents.supabase.functions.JchuSupabaseFunctions
import com.jeluchu.jchucomponents.supabase.realtime.JchuSupabaseRealtime
import io.github.jan.supabase.SupabaseClient

class JchuSupabase internal constructor(
    val client: SupabaseClient,
    val modules: JchuSupabaseModules
) {
    val auth: JchuSupabaseAuth by lazy {
        requireModule(enabled = modules.auth, name = "Auth")
        JchuSupabaseAuth(client)
    }
    val database: JchuSupabaseDatabase by lazy {
        requireModule(enabled = modules.database, name = "Database")
        JchuSupabaseDatabase(client)
    }
    val functions: JchuSupabaseFunctions by lazy {
        requireModule(enabled = modules.functions, name = "Functions")
        JchuSupabaseFunctions(client)
    }
    val realtime: JchuSupabaseRealtime by lazy {
        requireModule(enabled = modules.realtime, name = "Realtime")
        JchuSupabaseRealtime(client)
    }

    private fun requireModule(
        enabled: Boolean,
        name: String
    ) {
        check(enabled) { "Supabase $name module is disabled in JchuSupabaseConfig." }
    }
}
