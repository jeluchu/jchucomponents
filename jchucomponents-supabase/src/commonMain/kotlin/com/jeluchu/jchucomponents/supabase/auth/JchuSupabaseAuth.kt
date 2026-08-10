package com.jeluchu.jchucomponents.supabase.auth

import com.jeluchu.jchucomponents.network.models.Resource
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.auth.FlowType
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.SignOutScope
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.parseSessionFromUrl
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionSource
import io.ktor.http.Url
import io.ktor.http.parseQueryString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.json.JsonObject

@OptIn(SupabaseInternal::class)
class JchuSupabaseAuth internal constructor(
    private val client: SupabaseClient
) {
    val sessionState: StateFlow<JchuSupabaseAuthState> =
        client.auth.sessionStatus
            .map { it.toJchuSupabaseAuthState() }
            .stateIn(
                scope = client.auth.authScope,
                started = SharingStarted.Eagerly,
                initialValue =
                    client.auth.sessionStatus.value
                        .toJchuSupabaseAuthState()
            )

    fun signInWithEmail(
        email: String,
        password: String,
        captchaToken: String? = null
    ): Flow<Resource<JchuSupabaseAuthFailure, JchuSupabaseAuthUser>> =
        supabaseAuthResource {
            requireCredentials(email = email, password = password)
            client.auth.signInWith(Email) {
                this.email = email
                this.password = password
                this.captchaToken = captchaToken
            }
            client.auth.currentUserOrNull()?.toJchuSupabaseAuthUser()
                ?: throw JchuSupabaseAuthInputException(
                    code = JchuSupabaseAuthFailureCode.UNKNOWN,
                    message = "Supabase did not return an authenticated user."
                )
        }

    fun signUpWithEmail(
        email: String,
        password: String,
        redirectUrl: String? = null,
        metadata: JsonObject? = null,
        captchaToken: String? = null
    ): Flow<Resource<JchuSupabaseAuthFailure, JchuSupabaseSignUpResult>> =
        supabaseAuthResource {
            requireCredentials(email = email, password = password)
            val pendingUser =
                client.auth.signUpWith(
                    provider = Email,
                    redirectUrl = redirectUrl
                ) {
                    this.email = email
                    this.password = password
                    data = metadata
                    this.captchaToken = captchaToken
                }
            val activeUser = client.auth.currentUserOrNull()
            val user =
                pendingUser ?: activeUser
                    ?: throw JchuSupabaseAuthInputException(
                        code = JchuSupabaseAuthFailureCode.UNKNOWN,
                        message = "Supabase did not return the registered user."
                    )

            JchuSupabaseSignUpResult(
                user = user.toJchuSupabaseAuthUser(),
                requiresEmailConfirmation = client.auth.currentSessionOrNull() == null
            )
        }

    fun resendSignUpConfirmation(
        email: String,
        redirectUrl: String? = null,
        captchaToken: String? = null
    ): Flow<Resource<JchuSupabaseAuthFailure, Unit>> =
        supabaseAuthResource {
            requireEmail(email)
            client.auth.resendEmail(
                type = OtpType.Email.SIGNUP,
                email = email,
                captchaToken = captchaToken,
                redirectUrl = redirectUrl
            )
        }

    fun requestPasswordReset(
        email: String,
        redirectUrl: String? = null,
        captchaToken: String? = null
    ): Flow<Resource<JchuSupabaseAuthFailure, Unit>> =
        supabaseAuthResource {
            requireEmail(email)
            client.auth.resetPasswordForEmail(
                email = email,
                redirectUrl = redirectUrl,
                captchaToken = captchaToken
            )
        }

    fun updatePassword(
        newPassword: String,
        currentPassword: String? = null,
        nonce: String? = null
    ): Flow<Resource<JchuSupabaseAuthFailure, JchuSupabaseAuthUser>> =
        supabaseAuthResource {
            if (newPassword.isBlank()) {
                throw JchuSupabaseAuthInputException(
                    code = JchuSupabaseAuthFailureCode.INVALID_INPUT,
                    message = "New password cannot be blank."
                )
            }
            client.auth
                .updateUser {
                    password = newPassword
                    this.currentPassword = currentPassword
                    this.nonce = nonce
                }.toJchuSupabaseAuthUser()
        }

    fun refreshSession(): Flow<Resource<JchuSupabaseAuthFailure, JchuSupabaseAuthUser>> =
        supabaseAuthResource {
            client.auth.refreshCurrentSession()
            client.auth.currentUserOrNull()?.toJchuSupabaseAuthUser()
                ?: throw JchuSupabaseAuthInputException(
                    code = JchuSupabaseAuthFailureCode.SESSION_EXPIRED,
                    message = "No authenticated user is available."
                )
        }

    fun restoreSession(autoRefresh: Boolean = true): Flow<Resource<JchuSupabaseAuthFailure, Boolean>> =
        supabaseAuthResource {
            client.auth.loadFromStorage(autoRefresh = autoRefresh)
        }

    fun handleAuthCallback(url: String): Flow<Resource<JchuSupabaseAuthFailure, JchuSupabaseAuthUser>> =
        supabaseAuthResource {
            val callbackUrl = validateCallbackUrl(url)
            val user =
                when (client.auth.config.flowType) {
                    FlowType.PKCE -> {
                        val code =
                            callbackUrl.parameters["code"]
                                ?: throw invalidCallback("Authentication callback does not contain a code.")
                        client.auth.exchangeCodeForSession(code).user
                            ?: client.auth.currentUserOrNull()
                    }

                    FlowType.IMPLICIT -> {
                        val session =
                            try {
                                client.auth.parseSessionFromUrl(url)
                            } catch (_: IllegalArgumentException) {
                                throw invalidCallback("Authentication callback does not contain a valid session.")
                            }
                        val callbackUser = client.auth.retrieveUser(session.accessToken)
                        client.auth.importSession(
                            session = session.copy(user = callbackUser),
                            source = SessionSource.External
                        )
                        callbackUser
                    }
                }

            user?.toJchuSupabaseAuthUser()
                ?: throw invalidCallback("Authentication callback did not create a session.")
        }

    fun signOut(scope: JchuSupabaseSignOutScope = JchuSupabaseSignOutScope.LOCAL): Flow<Resource<JchuSupabaseAuthFailure, Unit>> =
        supabaseAuthResource {
            client.auth.signOut(
                scope =
                    when (scope) {
                        JchuSupabaseSignOutScope.LOCAL -> SignOutScope.LOCAL
                        JchuSupabaseSignOutScope.OTHERS -> SignOutScope.OTHERS
                        JchuSupabaseSignOutScope.GLOBAL -> SignOutScope.GLOBAL
                    }
            )
        }

    fun clearLocalSession(): Flow<Resource<JchuSupabaseAuthFailure, Unit>> =
        supabaseAuthResource {
            client.auth.clearSession()
        }

    suspend fun awaitInitialization(): JchuSupabaseAuthState {
        client.auth.awaitInitialization()
        return client.auth.sessionStatus.value
            .toJchuSupabaseAuthState()
    }

    fun currentUser(): JchuSupabaseAuthUser? = client.auth.currentUserOrNull()?.toJchuSupabaseAuthUser()

    fun currentUserId(): String? = currentUser()?.id

    private fun validateCallbackUrl(url: String): Url =
        validateAuthCallbackUrl(
            url = url,
            expectedScheme = client.auth.config.scheme,
            expectedHost = client.auth.config.host
        )

    private fun requireCredentials(
        email: String,
        password: String
    ) {
        requireEmail(email)
        if (password.isBlank()) {
            throw JchuSupabaseAuthInputException(
                code = JchuSupabaseAuthFailureCode.INVALID_INPUT,
                message = "Password cannot be blank."
            )
        }
    }

    private fun requireEmail(email: String) {
        if (email.isBlank()) {
            throw JchuSupabaseAuthInputException(
                code = JchuSupabaseAuthFailureCode.INVALID_INPUT,
                message = "Email cannot be blank."
            )
        }
    }

    private fun invalidCallback(message: String): JchuSupabaseAuthInputException = invalidAuthCallback(message)
}

internal fun validateAuthCallbackUrl(
    url: String,
    expectedScheme: String?,
    expectedHost: String?
): Url {
    if (url.isBlank()) throw invalidAuthCallback("Authentication callback URL cannot be blank.")
    val parsed =
        try {
            Url(url)
        } catch (_: IllegalArgumentException) {
            throw invalidAuthCallback("Authentication callback URL is invalid.")
        }
    if (expectedScheme != null && !parsed.protocol.name.equals(expectedScheme, ignoreCase = true)) {
        throw invalidAuthCallback("Authentication callback scheme does not match the configured scheme.")
    }
    if (expectedHost != null && !parsed.host.equals(expectedHost, ignoreCase = true)) {
        throw invalidAuthCallback("Authentication callback host does not match the configured host.")
    }
    val fragmentParameters =
        parseQueryString(
            query = url.substringAfter(delimiter = "#", missingDelimiterValue = "")
        )
    val callbackError =
        parsed.parameters["error_description"]
            ?: parsed.parameters["error"]
            ?: fragmentParameters["error_description"]
            ?: fragmentParameters["error"]
    if (callbackError != null) throw invalidAuthCallback(callbackError)
    return parsed
}

private fun invalidAuthCallback(message: String): JchuSupabaseAuthInputException =
    JchuSupabaseAuthInputException(
        code = JchuSupabaseAuthFailureCode.CALLBACK_INVALID,
        message = message
    )
