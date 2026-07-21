package com.jeluchu.jchucomponents.supabase.functions

import com.jeluchu.jchucomponents.network.models.Failure
import com.jeluchu.jchucomponents.network.models.Resource
import com.jeluchu.jchucomponents.supabase.flow.supabaseResource
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.functions.functions
import io.ktor.client.call.body
import kotlinx.coroutines.flow.Flow

class JchuSupabaseFunctions internal constructor(
    @PublishedApi
    internal val client: SupabaseClient
) {
    inline fun <reified Response : Any> invoke(
        name: String
    ): Flow<Resource<Failure, Response>> =
        supabaseResource {
            client.functions.invoke(name).body<Response>()
        }

    inline fun <reified Request : Any, reified Response : Any> invoke(
        name: String,
        body: Request
    ): Flow<Resource<Failure, Response>> =
        supabaseResource {
            client.functions.invoke(
                function = name,
                body = body
            ).body<Response>()
        }
}
