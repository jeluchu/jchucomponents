package com.jeluchu.jchucomponents.supabase.auth

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class JchuSupabaseAuthCallbackTest {
    @Test
    fun callbackAcceptsConfiguredSchemeAndHostIgnoringCase() {
        val callback =
            validateAuthCallbackUrl(
                url = "laarroba://auth-callback?code=confirmation-code",
                expectedScheme = "LAARROBA",
                expectedHost = "AUTH-CALLBACK"
            )

        assertEquals(expected = "confirmation-code", actual = callback.parameters["code"])
    }

    @Test
    fun callbackRejectsUnexpectedSchemeOrHost() {
        assertFailsWith<JchuSupabaseAuthInputException> {
            validateAuthCallbackUrl(
                url = "other://auth-callback?code=confirmation-code",
                expectedScheme = "laarroba",
                expectedHost = "auth-callback"
            )
        }
        assertFailsWith<JchuSupabaseAuthInputException> {
            validateAuthCallbackUrl(
                url = "laarroba://unexpected?code=confirmation-code",
                expectedScheme = "laarroba",
                expectedHost = "auth-callback"
            )
        }
    }

    @Test
    fun callbackDecodesErrorsFromImplicitFragment() {
        val error =
            assertFailsWith<JchuSupabaseAuthInputException> {
                validateAuthCallbackUrl(
                    url = "laarroba://auth-callback#error_description=Link%20expired",
                    expectedScheme = "laarroba",
                    expectedHost = "auth-callback"
                )
            }

        assertEquals(expected = JchuSupabaseAuthFailureCode.CALLBACK_INVALID, actual = error.code)
        assertEquals(expected = "Link expired", actual = error.message)
    }
}
