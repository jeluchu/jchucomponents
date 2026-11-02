package com.jeluchu.jchucomponents.supabase

import com.jeluchu.jchucomponents.supabase.auth.JchuSupabaseAuth
import com.jeluchu.jchucomponents.supabase.database.JchuSupabaseDatabase
import com.jeluchu.jchucomponents.supabase.functions.JchuSupabaseFunctions
import com.jeluchu.jchucomponents.supabase.realtime.JchuSupabaseRealtime
import com.jeluchu.jchucomponents.network.models.Failure
import com.jeluchu.jchucomponents.network.models.Resource
import com.jeluchu.jchucomponents.supabase.flow.awaitSessionRestored
import com.jeluchu.jchucomponents.supabase.flow.supabaseResource
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.flow.Flow

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

    /** Suspends until the stored auth session has been restored (no-op when Auth is disabled). */
    suspend fun awaitSessionRestored() = client.awaitSessionRestored()

    /** Like [supabaseResource], but waits for the stored session before running [block]. */
    inline fun <T> resource(
        crossinline block: suspend () -> T
    ): Flow<Resource<Failure, T>> =
        supabaseResource {
            awaitSessionRestored()
            block()
        }

    private fun requireModule(
        enabled: Boolean,
        name: String
    ) {
        check(enabled) { "Supabase $name module is disabled in JchuSupabaseConfig." }
    }
}
