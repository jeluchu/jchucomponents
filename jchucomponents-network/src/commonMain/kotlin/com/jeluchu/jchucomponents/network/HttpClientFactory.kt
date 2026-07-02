package com.jeluchu.jchucomponents.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Creates a configured Ktor client backed by the Android engine on Android and
 * the Darwin engine on Apple platforms.
 *
 * The caller owns the returned client and must close it when it is no longer needed.
 */
public fun createHttpClient(
    configuration: HttpClientConfiguration = HttpClientConfiguration(),
): HttpClient = createPlatformHttpClient(configuration)

internal expect fun createPlatformHttpClient(
    configuration: HttpClientConfiguration,
): HttpClient

internal fun <T : HttpClientEngineConfig> HttpClientConfig<T>.applyJchuConfiguration(
    configuration: HttpClientConfiguration,
    platformLogger: Logger,
) {
    expectSuccess = configuration.expectSuccess

    install(ContentNegotiation) {
        json(
            json = Json {
                coerceInputValues = configuration.coerceInputValues
                ignoreUnknownKeys = configuration.ignoreUnknownKeys
                isLenient = configuration.isLenient
            },
            contentType = ContentType.Any,
        )
    }

    if (configuration.enableCache) install(HttpCache)

    install(HttpTimeout) {
        requestTimeoutMillis = configuration.requestTimeoutMillis
        connectTimeoutMillis = configuration.connectTimeoutMillis
        socketTimeoutMillis = configuration.socketTimeoutMillis
    }

    if (configuration.enableLogging) {
        install(Logging) {
            logger = platformLogger
            level = configuration.logLevel.toKtorLogLevel()
            sanitizeHeader { header ->
                configuration.sensitiveHeaders.any {
                    it.equals(header, ignoreCase = true)
                }
            }
        }
    }

    defaultRequest {
        if (configuration.baseUrl.isNotBlank()) url(configuration.baseUrl)
        header(HttpHeaders.Accept, ContentType.Application.Json)
        header(HttpHeaders.ContentType, ContentType.Application.Json)
        configuration.defaultHeaders.forEach { (key, value) -> header(key, value) }
    }
}

private fun HttpClientLogLevel.toKtorLogLevel(): LogLevel = when (this) {
    HttpClientLogLevel.NONE -> LogLevel.NONE
    HttpClientLogLevel.INFO -> LogLevel.INFO
    HttpClientLogLevel.HEADERS -> LogLevel.HEADERS
    HttpClientLogLevel.BODY -> LogLevel.BODY
    HttpClientLogLevel.ALL -> LogLevel.ALL
}
