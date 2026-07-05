package com.jeluchu.jchucomponents.network.http

/**
 * Shared configuration for the Ktor client created by [createHttpClient].
 *
 * The factory selects the Android or Darwin engine for the current platform.
 */
data class HttpClientConfiguration(
    val baseUrl: String = "",
    val isLenient: Boolean = true,
    val enableCache: Boolean = true,
    val expectSuccess: Boolean = true,
    val enableLogging: Boolean = false,
    val coerceInputValues: Boolean = true,
    val ignoreUnknownKeys: Boolean = true,
    val defaultHeaders: Map<String, String> = emptyMap(),
    val socketTimeoutMillis: Long = DEFAULT_TIMEOUT_MILLIS,
    val requestTimeoutMillis: Long = DEFAULT_TIMEOUT_MILLIS,
    val connectTimeoutMillis: Long = DEFAULT_TIMEOUT_MILLIS,
    val logLevel: HttpClientLogLevel = HttpClientLogLevel.ALL,
    val sensitiveHeaders: Set<String> = setOf("Authorization"),
) {
    companion object {
        const val DEFAULT_TIMEOUT_MILLIS: Long = 90_000
    }
}

/** Logging detail used by clients created with [createHttpClient]. */
enum class HttpClientLogLevel {
    NONE,
    INFO,
    HEADERS,
    BODY,
    ALL,
}
