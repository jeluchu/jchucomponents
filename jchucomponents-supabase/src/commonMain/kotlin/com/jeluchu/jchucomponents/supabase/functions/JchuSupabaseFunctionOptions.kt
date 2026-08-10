package com.jeluchu.jchucomponents.supabase.functions

import io.ktor.http.Headers

data class JchuSupabaseFunctionOptions(
    val headers: Map<String, String> = emptyMap(),
    val idempotencyKey: String? = null,
    val correlationId: String? = null
) {
    init {
        headers.forEach { (name, value) ->
            require(name.isNotBlank()) { "Edge Function header name cannot be blank." }
            require(value.isNotBlank()) { "Edge Function header value cannot be blank." }
            require(name.lowercase() !in PROTECTED_HEADERS) {
                "The $name header is managed by the Supabase client."
            }
        }
        require(idempotencyKey == null || idempotencyKey.isNotBlank()) {
            "Edge Function idempotency key cannot be blank."
        }
        require(correlationId == null || correlationId.isNotBlank()) {
            "Edge Function correlation id cannot be blank."
        }
    }

    @PublishedApi
    internal fun toKtorHeaders(): Headers =
        Headers.build {
            headers.forEach { (name, value) -> append(name, value) }
            idempotencyKey?.let { append(IDEMPOTENCY_KEY_HEADER, it) }
            correlationId?.let { append(CORRELATION_ID_HEADER, it) }
        }

    companion object {
        const val IDEMPOTENCY_KEY_HEADER: String = "Idempotency-Key"
        const val CORRELATION_ID_HEADER: String = "X-Correlation-Id"

        private val PROTECTED_HEADERS =
            setOf(
                "authorization",
                "apikey",
                IDEMPOTENCY_KEY_HEADER.lowercase(),
                CORRELATION_ID_HEADER.lowercase()
            )
    }
}
