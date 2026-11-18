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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

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

    private val refreshMutex = Mutex()
    private var lastRefreshMark: TimeSource.Monotonic.ValueTimeMark? = null

    /**
     * Forces a session refresh after the backend rejected an expired access token. Concurrent callers
     * share one refresh: a caller arriving within [reuseWindow] of a successful refresh reuses it.
     * Returns `false` when Auth is disabled, there is no session or the refresh token was rejected.
     */
    suspend fun refreshExpiredSession(reuseWindow: Duration = 10.seconds): Boolean {
        if (!modules.auth) return false
        return refreshMutex.withLock {
            val recentlyRefreshed = lastRefreshMark?.let { it.elapsedNow() < reuseWindow } == true
            if (recentlyRefreshed) return@withLock true
            val refreshed = auth.refreshSession().first { it !is Resource.Loading } is Resource.Success
            if (refreshed) lastRefreshMark = TimeSource.Monotonic.markNow()
            refreshed
        }
    }

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
