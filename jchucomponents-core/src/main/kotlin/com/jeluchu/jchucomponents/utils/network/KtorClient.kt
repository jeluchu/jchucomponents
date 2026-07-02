/*
 *
 *  Copyright 2026 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.utils.network

import android.os.Build
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import java.text.Normalizer
import java.util.Locale
import java.util.TimeZone
import kotlinx.serialization.json.Json

/**
 * Creates the standard Ktor client used by Android consumers.
 */
public object KtorClient {
    private const val DEFAULT_TIMEOUT_MILLIS: Long = 90_000

    public fun buildHttpClient(
        baseUrl: String = "",
        headers: ClientHeaders? = null,
        isDebug: Boolean = false,
        timeoutMillis: Long = DEFAULT_TIMEOUT_MILLIS,
    ): HttpClient = HttpClient(Android) {
        expectSuccess = true

        install(ContentNegotiation) {
            json(
                Json {
                    coerceInputValues = true
                    ignoreUnknownKeys = true
                    isLenient = true
                }
            )
        }

        install(HttpTimeout) {
            requestTimeoutMillis = timeoutMillis
            connectTimeoutMillis = timeoutMillis
            socketTimeoutMillis = timeoutMillis
        }

        if (isDebug) {
            install(Logging) {
                logger = Logger.ANDROID
                level = LogLevel.ALL
                sanitizeHeader { header ->
                    header == HttpHeaders.Authorization ||
                        header.equals(headers?.keyHeader, ignoreCase = true)
                }
            }
        }

        defaultRequest {
            if (baseUrl.isNotBlank()) url(baseUrl)
            header(HttpHeaders.Accept, ContentType.Application.Json)
            header(HttpHeaders.ContentType, ContentType.Application.Json)

            headers?.let { clientHeaders ->
                header(HttpHeaders.UserAgent, clientHeaders.userAgent.value)
                header("X-Client", "${clientHeaders.client}-android")
                header("Accept-Language", Locale.getDefault().toLanguageTag())
                header("X-Request-AppVersion", clientHeaders.userAgent.versionName)
                header("X-Request-OsVersion", osVersion)
                header("X-Request-Device", deviceName)
                header("X-Mobile-Native", "Android")
                header("X-User-TimezoneOffset", TimeZone.getDefault().id)
                if (clientHeaders.key.isNotEmpty() && clientHeaders.keyHeader.isNotEmpty()) {
                    header(clientHeaders.keyHeader, clientHeaders.key)
                }
            }
        }
    }

    private val ClientHeaders.UserAgent.value: String
        get() = "$appName/$versionName (rv $versionCode) ktor"

    private val deviceName: String
        get() {
            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL
            val value = if (model.startsWith(manufacturer, ignoreCase = true)) model else "$manufacturer $model"
            return Normalizer.normalize(value, Normalizer.Form.NFD)
                .replace("[^\\x00-\\x7F]".toRegex(), "")
        }

    private val osVersion: String
        get() = "Android ${Build.VERSION.RELEASE} (${Build.VERSION.SDK_INT})"
}
