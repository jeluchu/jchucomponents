package com.jeluchu.jchucomponents.network.http

import com.jeluchu.jchucomponents.config.JchuConfigBuilder
import com.jeluchu.jchucomponents.config.JchuFeature
import com.jeluchu.jchucomponents.config.JchuPlatform
import io.ktor.client.HttpClient
import org.koin.core.module.Module
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
            sensitiveHeaders = sensitiveHeaders
        )
}

/**
 * Registers a shared Ktor [HttpClient] built with [createHttpClient]; it is closed when Koin stops.
 *
 * ```
 * JchuConfig {
 *     network { baseUrl = "https://api.example.com"; enableLogging = true }
 * }
 * ```
 */
fun JchuConfigBuilder.network(block: JchuNetworkBlock.() -> Unit = {}) {
    install(JchuNetworkFeature(JchuNetworkBlock().apply(block).toConfiguration()))
}

internal class JchuNetworkFeature(
    private val configuration: HttpClientConfiguration
) : JchuFeature {
    override val id: String = ID

    override fun modules(platform: JchuPlatform): List<Module> =
        listOf(
            module {
                single<HttpClient> { createHttpClient(configuration) } onClose { it?.close() }
            }
        )

    companion object {
        const val ID = "network"
    }
}
