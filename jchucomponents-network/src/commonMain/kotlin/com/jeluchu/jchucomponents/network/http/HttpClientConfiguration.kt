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
    /** Follow HTTP redirects. Turn it off to inspect redirects yourself (for example link previews). */
    val followRedirects: Boolean = true,
    /** Install JSON content negotiation and the JSON `Accept`/`Content-Type` headers. Off for raw downloads. */
    val useJson: Boolean = true,
    /** Engine to use where there is a choice, see [JchuHttpEngine]. */
    val engine: JchuHttpEngine = JchuHttpEngine.Default,
    /** Apple only: false keeps the client off mobile data (`allowsCellularAccess`). */
    val allowCellularAccess: Boolean = true,
    /**
     * Headers computed for every request, after [defaultHeaders], so a value that changes while the app runs
     * (a token, a key fetched later) is always current. A key in both replaces the static value.
     */
    val headerProvider: (() -> Map<String, String>)? = null
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
    ALL
}

/** Network engine of a client. Only Android has a real choice; Apple always uses Darwin and JVM always CIO. */
enum class JchuHttpEngine {
    /** The platform default: Android (`HttpURLConnection`), Darwin or CIO. */
    Default,

    /** Pure Kotlin CIO engine on Android, for downloads that must not depend on the platform networking stack. */
    Cio
}
