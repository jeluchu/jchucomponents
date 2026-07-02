package com.jeluchu.jchucomponents.network

/**
 * Shared configuration for the Ktor client created by [createHttpClient].
 *
 * The factory selects the Android or Darwin engine for the current platform.
 */
public data class HttpClientConfiguration(
    public val baseUrl: String = "",
    public val requestTimeoutMillis: Long = DEFAULT_TIMEOUT_MILLIS,
    public val connectTimeoutMillis: Long = DEFAULT_TIMEOUT_MILLIS,
    public val socketTimeoutMillis: Long = DEFAULT_TIMEOUT_MILLIS,
    public val expectSuccess: Boolean = true,
    public val enableCache: Boolean = true,
    public val enableLogging: Boolean = false,
    public val logLevel: HttpClientLogLevel = HttpClientLogLevel.ALL,
    public val defaultHeaders: Map<String, String> = emptyMap(),
    public val sensitiveHeaders: Set<String> = setOf("Authorization"),
    public val coerceInputValues: Boolean = true,
    public val ignoreUnknownKeys: Boolean = true,
    public val isLenient: Boolean = true,
) {
    public companion object {
        public const val DEFAULT_TIMEOUT_MILLIS: Long = 90_000
    }
}

/** Logging detail used by clients created with [createHttpClient]. */
public enum class HttpClientLogLevel {
    NONE,
    INFO,
    HEADERS,
    BODY,
    ALL,
}
