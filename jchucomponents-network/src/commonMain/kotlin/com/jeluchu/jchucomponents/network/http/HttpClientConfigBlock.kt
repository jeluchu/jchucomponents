package com.jeluchu.jchucomponents.network.http

import com.jeluchu.jchucomponents.config.JchuConfigBuilder
import com.jeluchu.jchucomponents.config.JchuFeature
import com.jeluchu.jchucomponents.config.JchuPlatform
import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.dsl.onClose

/**
 * Options of the `network { }` block of `JchuConfig`; they mirror [HttpClientConfiguration].
 */
class JchuNetworkBlock {
    var baseUrl: String = ""
    var isLenient: Boolean = true
    var enableCache: Boolean = true
    var expectSuccess: Boolean = true
    var enableLogging: Boolean = false
    var coerceInputValues: Boolean = true
    var ignoreUnknownKeys: Boolean = true
    var defaultHeaders: Map<String, String> = emptyMap()
    var socketTimeoutMillis: Long = HttpClientConfiguration.DEFAULT_TIMEOUT_MILLIS
    var requestTimeoutMillis: Long = HttpClientConfiguration.DEFAULT_TIMEOUT_MILLIS
    var connectTimeoutMillis: Long = HttpClientConfiguration.DEFAULT_TIMEOUT_MILLIS
    var logLevel: HttpClientLogLevel = HttpClientLogLevel.ALL
    var sensitiveHeaders: Set<String> = setOf("Authorization")
    var followRedirects: Boolean = true
    var useJson: Boolean = true
    var engine: JchuHttpEngine = JchuHttpEngine.Default
    var allowCellularAccess: Boolean = true

    private val dynamicHeaders = mutableListOf<Pair<String, () -> String>>()

    /**
     * Sends [name] with the value [value] returns at the moment of each request, so it can change while the app
     * runs. A blank value omits the header.
     *
     * ```
     * network { header("x-api-key") { ApiKeyStore.current } }
     * ```
     */
    fun header(name: String, value: () -> String) {
        require(name.isNotBlank()) { "JchuConfig: network header name cannot be blank." }
        dynamicHeaders += name to value
    }

    internal fun toConfiguration() =
        HttpClientConfiguration(
            baseUrl = baseUrl,
            isLenient = isLenient,
            enableCache = enableCache,
            expectSuccess = expectSuccess,
            enableLogging = enableLogging,
            coerceInputValues = coerceInputValues,
            ignoreUnknownKeys = ignoreUnknownKeys,
            defaultHeaders = defaultHeaders,
            socketTimeoutMillis = socketTimeoutMillis,
            requestTimeoutMillis = requestTimeoutMillis,
            connectTimeoutMillis = connectTimeoutMillis,
            logLevel = logLevel,
            sensitiveHeaders = sensitiveHeaders,
            followRedirects = followRedirects,
            useJson = useJson,
            engine = engine,
            allowCellularAccess = allowCellularAccess,
            headerProvider =
                dynamicHeaders.takeIf { it.isNotEmpty() }?.toList()?.let { list ->
                    { list.mapNotNull { (name, value) -> value().takeIf { it.isNotBlank() }?.let { name to it } }.toMap() }
                }
        )
}

/**
 * Registers a shared Ktor [HttpClient] built with [createHttpClient]; it is closed when Koin stops.
 *
 * ```
 * JchuConfig {
 *     network { baseUrl = "https://api.example.com"; enableLogging = true }
 *     // A second client, injected with `get<HttpClient>(named("downloads"))`:
 *     network("downloads") { useJson = false; requestTimeoutMillis = 0; allowCellularAccess = false }
 * }
 * ```
 *
 * Without [name] the client is the plain `HttpClient`. With a [name] it is registered under `named(name)`,
 * so an app can have several clients with different settings.
 */
fun JchuConfigBuilder.network(
    name: String? = null,
    block: JchuNetworkBlock.() -> Unit = {}
) {
    require(name == null || name.isNotBlank()) { "JchuConfig: network client name cannot be blank." }
    install(JchuNetworkFeature(name, JchuNetworkBlock().apply(block).toConfiguration()))
}

internal class JchuNetworkFeature(
    private val name: String?,
    private val configuration: HttpClientConfiguration
) : JchuFeature {
    override val id: String = if (name == null) ID else "$ID:$name"

    override fun modules(platform: JchuPlatform): List<Module> =
        listOf(
            module {
                if (name == null) {
                    single<HttpClient> { createHttpClient(configuration) } onClose { it?.close() }
                } else {
                    single<HttpClient>(named(name)) { createHttpClient(configuration) } onClose { it?.close() }
                }
            }
        )

    companion object {
        const val ID = "network"
    }
}
