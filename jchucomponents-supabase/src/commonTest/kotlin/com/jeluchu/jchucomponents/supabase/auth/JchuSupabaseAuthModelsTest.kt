package com.jeluchu.jchucomponents.supabase.auth

import io.github.jan.supabase.auth.status.SessionSource
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.auth.user.UserSession
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.time.Instant

class JchuSupabaseAuthModelsTest {
    @Test
    fun userMappingExposesVerificationWithoutTokens() {
        val confirmedAt = Instant.parse("2026-08-10T12:00:00Z")
        val mapped =
            UserInfo(
                aud = "authenticated",
                id = "user-id",
                email = "user@example.com",
                emailConfirmedAt = confirmedAt
            ).toJchuSupabaseAuthUser()

        assertEquals(expected = "user-id", actual = mapped.id)
        assertEquals(expected = "user@example.com", actual = mapped.email)
        assertTrue(mapped.isEmailVerified)
        assertFalse(mapped.isAnonymous)
    }

    @Test
    fun authenticatedSessionMappingKeepsOnlySafeSessionState() {
        val session =
            UserSession(
                accessToken = "access-token",
                refreshToken = "refresh-token",
                expiresIn = 3600,
                tokenType = "bearer",
                user = UserInfo(aud = "authenticated", id = "user-id")
            )

        val mapped =
            SessionStatus
                .Authenticated(
                    session = session,
                    source = SessionSource.Storage
                ).toJchuSupabaseAuthState()

        val signedIn = assertIs<JchuSupabaseAuthState.SignedIn>(mapped)
        assertEquals(expected = "user-id", actual = signedIn.user?.id)
        assertEquals(expected = JchuSupabaseSessionSource.STORAGE, actual = signedIn.source)
        assertFalse(signedIn.isNewSession)
    }

    @Test
    fun inputFailuresKeepAStableMachineReadableCode() {
        val failure =
            JchuSupabaseAuthInputException(
                code = JchuSupabaseAuthFailureCode.CALLBACK_INVALID,
                message = "Invalid callback"
            ).toJchuSupabaseAuthFailure()

        assertEquals(expected = JchuSupabaseAuthFailureCode.CALLBACK_INVALID, actual = failure.code)
        assertEquals(expected = "Invalid callback", actual = failure.message)
    }
}
