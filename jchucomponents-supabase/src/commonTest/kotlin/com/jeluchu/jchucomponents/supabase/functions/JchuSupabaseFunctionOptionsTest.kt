package com.jeluchu.jchucomponents.supabase.functions

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class JchuSupabaseFunctionOptionsTest {
    @Test
    fun optionsAddOperationalHeaders() {
        val headers =
            JchuSupabaseFunctionOptions(
                headers = mapOf("X-Client-Version" to "3.0.0-alpha11"),
                idempotencyKey = "operation-id",
                correlationId = "request-id"
            ).toKtorHeaders()

        assertEquals(expected = "3.0.0-alpha11", actual = headers["X-Client-Version"])
        assertEquals(expected = "operation-id", actual = headers[JchuSupabaseFunctionOptions.IDEMPOTENCY_KEY_HEADER])
        assertEquals(expected = "request-id", actual = headers[JchuSupabaseFunctionOptions.CORRELATION_ID_HEADER])
    }

    @Test
    fun optionsRejectAuthenticationHeaderOverrides() {
        assertFailsWith<IllegalArgumentException> {
            JchuSupabaseFunctionOptions(headers = mapOf("Authorization" to "Bearer unsafe"))
        }
        assertFailsWith<IllegalArgumentException> {
            JchuSupabaseFunctionOptions(headers = mapOf("apikey" to "unsafe"))
        }
        assertFailsWith<IllegalArgumentException> {
            JchuSupabaseFunctionOptions(headers = mapOf("Idempotency-Key" to "duplicate"))
        }
    }

    @Test
    fun optionsRejectBlankOperationIdentifiers() {
        assertFailsWith<IllegalArgumentException> {
            JchuSupabaseFunctionOptions(idempotencyKey = " ")
        }
        assertFailsWith<IllegalArgumentException> {
            JchuSupabaseFunctionOptions(correlationId = "")
        }
    }
}
