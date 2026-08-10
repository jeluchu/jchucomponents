package com.jeluchu.jchucomponents.supabase.auth

import com.jeluchu.jchucomponents.network.models.Resource
import io.github.jan.supabase.auth.exception.AuthErrorCode
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.auth.exception.AuthWeakPasswordException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.io.IOException

data class JchuSupabaseAuthFailure(
    val code: JchuSupabaseAuthFailureCode,
    val message: String,
    val statusCode: Int? = null,
    val rawCode: String? = null,
    val weakPasswordReasons: List<String> = emptyList(),
    val retryable: Boolean = false
)

enum class JchuSupabaseAuthFailureCode {
    INVALID_INPUT,
    INVALID_CREDENTIALS,
    EMAIL_NOT_CONFIRMED,
    ACCOUNT_ALREADY_EXISTS,
    WEAK_PASSWORD,
    SIGN_UP_DISABLED,
    PROVIDER_DISABLED,
    USER_BANNED,
    CAPTCHA_FAILED,
    RATE_LIMITED,
    SESSION_EXPIRED,
    INVALID_EMAIL,
    PASSWORD_UNCHANGED,
    REAUTHENTICATION_REQUIRED,
    CALLBACK_INVALID,
    NETWORK,
    TIMEOUT,
    SERVER,
    UNKNOWN
}

internal class JchuSupabaseAuthInputException(
    val code: JchuSupabaseAuthFailureCode,
    message: String
) : IllegalArgumentException(message)

internal fun <T> supabaseAuthResource(block: suspend () -> T): Flow<Resource<JchuSupabaseAuthFailure, T>> =
    flow<Resource<JchuSupabaseAuthFailure, T>> {
        emit(Resource.Loading())
        emit(Resource.Success(block()))
    }.catch { throwable ->
        if (throwable is CancellationException) throw throwable
        emit(Resource.Error(throwable.toJchuSupabaseAuthFailure()))
    }

internal fun Throwable.toJchuSupabaseAuthFailure(): JchuSupabaseAuthFailure =
    when (this) {
        is JchuSupabaseAuthInputException ->
            JchuSupabaseAuthFailure(
                code = code,
                message = message.orEmpty()
            )
        is AuthWeakPasswordException ->
            toAuthRestFailure(
                code = JchuSupabaseAuthFailureCode.WEAK_PASSWORD,
                weakPasswordReasons = reasons
            )
        is AuthRestException -> toAuthRestFailure()
        is HttpRequestTimeoutException ->
            JchuSupabaseAuthFailure(
                code = JchuSupabaseAuthFailureCode.TIMEOUT,
                message = message.orEmpty().ifBlank { "Authentication request timed out." },
                retryable = true
            )
        is ResponseException ->
            JchuSupabaseAuthFailure(
                code =
                    if (response.status.value >= 500) {
                        JchuSupabaseAuthFailureCode.SERVER
                    } else {
                        JchuSupabaseAuthFailureCode.UNKNOWN
                    },
                message = message.orEmpty().ifBlank { response.status.description },
                statusCode = response.status.value,
                retryable = response.status.value >= 500
            )
        is IOException ->
            JchuSupabaseAuthFailure(
                code = JchuSupabaseAuthFailureCode.NETWORK,
                message = message.orEmpty().ifBlank { "Network connection failed." },
                retryable = true
            )
        else ->
            JchuSupabaseAuthFailure(
                code =
                    if (message?.contains("timeout", ignoreCase = true) == true) {
                        JchuSupabaseAuthFailureCode.TIMEOUT
                    } else {
                        JchuSupabaseAuthFailureCode.UNKNOWN
                    },
                message = message.orEmpty().ifBlank { "Authentication failed." },
                retryable = message?.contains("timeout", ignoreCase = true) == true
            )
    }

private fun AuthRestException.toAuthRestFailure(
    code: JchuSupabaseAuthFailureCode = errorCode.toJchuSupabaseAuthFailureCode(),
    weakPasswordReasons: List<String> = emptyList()
): JchuSupabaseAuthFailure {
    val resolvedCode =
        when {
            code != JchuSupabaseAuthFailureCode.UNKNOWN -> code
            response.status.value == 408 -> JchuSupabaseAuthFailureCode.TIMEOUT
            response.status.value == 429 -> JchuSupabaseAuthFailureCode.RATE_LIMITED
            response.status.value >= 500 -> JchuSupabaseAuthFailureCode.SERVER
            else -> JchuSupabaseAuthFailureCode.UNKNOWN
        }
    return JchuSupabaseAuthFailure(
        code = resolvedCode,
        message = errorDescription.ifBlank { message.orEmpty().ifBlank { "Authentication failed." } },
        statusCode = response.status.value,
        rawCode = error,
        weakPasswordReasons = weakPasswordReasons,
        retryable =
            resolvedCode == JchuSupabaseAuthFailureCode.RATE_LIMITED ||
                resolvedCode == JchuSupabaseAuthFailureCode.TIMEOUT ||
                resolvedCode == JchuSupabaseAuthFailureCode.SERVER
    )
}

private fun AuthErrorCode?.toJchuSupabaseAuthFailureCode(): JchuSupabaseAuthFailureCode =
    when (this) {
        AuthErrorCode.InvalidCredentials,
        AuthErrorCode.UserNotFound -> JchuSupabaseAuthFailureCode.INVALID_CREDENTIALS
        AuthErrorCode.EmailNotConfirmed -> JchuSupabaseAuthFailureCode.EMAIL_NOT_CONFIRMED
        AuthErrorCode.EmailExists,
        AuthErrorCode.UserAlreadyExists -> JchuSupabaseAuthFailureCode.ACCOUNT_ALREADY_EXISTS
        AuthErrorCode.WeakPassword -> JchuSupabaseAuthFailureCode.WEAK_PASSWORD
        AuthErrorCode.SignupDisabled -> JchuSupabaseAuthFailureCode.SIGN_UP_DISABLED
        AuthErrorCode.EmailProviderDisabled,
        AuthErrorCode.ProviderDisabled -> JchuSupabaseAuthFailureCode.PROVIDER_DISABLED
        AuthErrorCode.UserBanned -> JchuSupabaseAuthFailureCode.USER_BANNED
        AuthErrorCode.CaptchaFailed -> JchuSupabaseAuthFailureCode.CAPTCHA_FAILED
        AuthErrorCode.OverRequestRateLimit,
        AuthErrorCode.OverEmailSendRateLimit,
        AuthErrorCode.OverSmsSendRateLimit -> JchuSupabaseAuthFailureCode.RATE_LIMITED
        AuthErrorCode.SessionExpired,
        AuthErrorCode.SessionNotFound,
        AuthErrorCode.RefreshTokenNotFound,
        AuthErrorCode.RefreshTokenAlreadyUsed -> JchuSupabaseAuthFailureCode.SESSION_EXPIRED
        AuthErrorCode.EmailAddressInvalid,
        AuthErrorCode.EmailAddressNotAuthorized -> JchuSupabaseAuthFailureCode.INVALID_EMAIL
        AuthErrorCode.SamePassword -> JchuSupabaseAuthFailureCode.PASSWORD_UNCHANGED
        AuthErrorCode.ReauthenticationNeeded,
        AuthErrorCode.ReauthenticationNotValid,
        AuthErrorCode.ReauthNonceMissing -> JchuSupabaseAuthFailureCode.REAUTHENTICATION_REQUIRED
        AuthErrorCode.BadCodeVerifier,
        AuthErrorCode.FlowStateExpired,
        AuthErrorCode.FlowStateNotFound,
        AuthErrorCode.OtpExpired,
        AuthErrorCode.BadOauthCallback,
        AuthErrorCode.BadOauthState -> JchuSupabaseAuthFailureCode.CALLBACK_INVALID
        AuthErrorCode.RequestTimeout,
        AuthErrorCode.HookTimeout -> JchuSupabaseAuthFailureCode.TIMEOUT
        AuthErrorCode.UnexpectedFailure,
        AuthErrorCode.HookTimeoutAfterRetry -> JchuSupabaseAuthFailureCode.SERVER
        else -> JchuSupabaseAuthFailureCode.UNKNOWN
    }
