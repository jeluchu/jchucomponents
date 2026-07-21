package com.jeluchu.jchucomponents.supabase.auth

import com.jeluchu.jchucomponents.network.models.Failure
import com.jeluchu.jchucomponents.network.models.Resource
import com.jeluchu.jchucomponents.supabase.flow.supabaseResource
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.flow.Flow

class JchuSupabaseAuth internal constructor(
    private val client: SupabaseClient
) {
    fun signInWithEmail(
        email: String,
        password: String
    ): Flow<Resource<Failure, Unit>> =
        supabaseResource {
            client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
        }

    fun signUpWithEmail(
        email: String,
        password: String,
        redirectUrl: String? = null
    ): Flow<Resource<Failure, Unit>> =
        supabaseResource {
            client.auth.signUpWith(
                provider = Email,
                redirectUrl = redirectUrl
            ) {
                this.email = email
                this.password = password
            }
        }

    fun signOut(): Flow<Resource<Failure, Unit>> =
        supabaseResource {
            client.auth.signOut()
        }

    fun currentUserId(): String? =
        client.auth.currentUserOrNull()?.id
}
