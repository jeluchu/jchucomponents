package com.jeluchu.jchucomponents.supabase.auth

import io.github.jan.supabase.auth.status.RefreshFailureCause
import io.github.jan.supabase.auth.status.SessionSource
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.serialization.json.JsonObject
import kotlin.time.Instant

data class JchuSupabaseAuthUser(
    val id: String,
    val email: String?,
    val emailConfirmedAt: Instant?,
    val phone: String?,
    val createdAt: Instant?,
    val updatedAt: Instant?,
    val userMetadata: JsonObject?,
    val appMetadata: JsonObject?,
    val isAnonymous: Boolean
) {
    val isEmailVerified: Boolean
        get() = emailConfirmedAt != null
}

data class JchuSupabaseSignUpResult(
    val user: JchuSupabaseAuthUser,
    val requiresEmailConfirmation: Boolean
)

sealed interface JchuSupabaseAuthState {
    data object Initializing : JchuSupabaseAuthState

    data class SignedOut(
        val isSignOut: Boolean
    ) : JchuSupabaseAuthState

    data class SignedIn(
        val user: JchuSupabaseAuthUser?,
        val expiresAt: Instant,
        val source: JchuSupabaseSessionSource,
        val isNewSession: Boolean
    ) : JchuSupabaseAuthState

    data class RefreshFailed(
        val cause: JchuSupabaseSessionRefreshFailure
    ) : JchuSupabaseAuthState
}

enum class JchuSupabaseSessionSource {
    STORAGE,
    ANONYMOUS_SIGN_IN,
    SIGN_IN,
    SIGN_UP,
    EXTERNAL,
    REFRESH,
    USER_CHANGED,
    USER_IDENTITIES_CHANGED,
    UNKNOWN
}

enum class JchuSupabaseSessionRefreshFailure {
    NETWORK,
    SERVER
}

enum class JchuSupabaseSignOutScope {
    LOCAL,
    OTHERS,
    GLOBAL
}

internal fun UserInfo.toJchuSupabaseAuthUser(): JchuSupabaseAuthUser =
    JchuSupabaseAuthUser(
        id = id,
        email = email,
        emailConfirmedAt = emailConfirmedAt,
        phone = phone,
        createdAt = createdAt,
        updatedAt = updatedAt,
        userMetadata = userMetadata,
        appMetadata = appMetadata,
        isAnonymous = isAnonymous == true
    )

@Suppress("DEPRECATION")
internal fun SessionStatus.toJchuSupabaseAuthState(): JchuSupabaseAuthState =
    when (this) {
        SessionStatus.Initializing -> JchuSupabaseAuthState.Initializing
        is SessionStatus.NotAuthenticated ->
            JchuSupabaseAuthState.SignedOut(isSignOut = isSignOut)
        is SessionStatus.Authenticated ->
            JchuSupabaseAuthState.SignedIn(
                user = session.user?.toJchuSupabaseAuthUser(),
                expiresAt = session.expiresAt,
                source = source.toJchuSupabaseSessionSource(),
                isNewSession = isNew
            )
        is SessionStatus.RefreshFailure ->
            JchuSupabaseAuthState.RefreshFailed(
                cause =
                    when (cause) {
                        is RefreshFailureCause.NetworkError -> JchuSupabaseSessionRefreshFailure.NETWORK
                        is RefreshFailureCause.InternalServerError -> JchuSupabaseSessionRefreshFailure.SERVER
                    }
            )
    }

private fun SessionSource.toJchuSupabaseSessionSource(): JchuSupabaseSessionSource =
    when (this) {
        SessionSource.Storage -> JchuSupabaseSessionSource.STORAGE
        SessionSource.AnonymousSignIn -> JchuSupabaseSessionSource.ANONYMOUS_SIGN_IN
        is SessionSource.SignIn -> JchuSupabaseSessionSource.SIGN_IN
        is SessionSource.SignUp -> JchuSupabaseSessionSource.SIGN_UP
        SessionSource.External -> JchuSupabaseSessionSource.EXTERNAL
        is SessionSource.Refresh -> JchuSupabaseSessionSource.REFRESH
        is SessionSource.UserChanged -> JchuSupabaseSessionSource.USER_CHANGED
        is SessionSource.UserIdentitiesChanged -> JchuSupabaseSessionSource.USER_IDENTITIES_CHANGED
        SessionSource.Unknown -> JchuSupabaseSessionSource.UNKNOWN
    }
