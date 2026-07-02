/*
 *
 *  Copyright 2026 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.utils.network

import android.os.Build
import io.ktor.client.HttpClient
import com.jeluchu.jchucomponents.network.HttpClientConfiguration
import com.jeluchu.jchucomponents.network.createHttpClient
import java.text.Normalizer
import java.util.Locale
import java.util.TimeZone

/**
 * Creates the standard Ktor client used by Android consumers.
 */
@Deprecated(
    message = "Use createHttpClient from jchucomponents-network",
    replaceWith = ReplaceWith(
        expression = "createHttpClient(HttpClientConfiguration(baseUrl = baseUrl, enableLogging = isDebug))",
        imports = [
            "com.jeluchu.jchucomponents.network.HttpClientConfiguration",
            "com.jeluchu.jchucomponents.network.createHttpClient",
        ],
    ),
)
public object KtorClient {
    private const val DEFAULT_TIMEOUT_MILLIS: Long = 90_000

    public fun buildHttpClient(
        baseUrl: String = "",
        headers: ClientHeaders? = null,
        isDebug: Boolean = false,
        timeoutMillis: Long = DEFAULT_TIMEOUT_MILLIS,
    ): HttpClient = createHttpClient(
        HttpClientConfiguration(
            baseUrl = baseUrl,
            requestTimeoutMillis = timeoutMillis,
            connectTimeoutMillis = timeoutMillis,
            socketTimeoutMillis = timeoutMillis,
            enableLogging = isDebug,
            defaultHeaders = headers?.toDefaultHeaders().orEmpty(),
            sensitiveHeaders = setOfNotNull(
                "Authorization",
                headers?.keyHeader?.takeIf(String::isNotEmpty),
            ),
        )
    )

    private fun ClientHeaders.toDefaultHeaders(): Map<String, String> = buildMap {
        put("User-Agent", userAgent.value)
        put("X-Client", "$client-android")
        put("Accept-Language", Locale.getDefault().toLanguageTag())
        put("X-Request-AppVersion", userAgent.versionName)
        put("X-Request-OsVersion", osVersion)
        put("X-Request-Device", deviceName)
        put("X-Mobile-Native", "Android")
        put("X-User-TimezoneOffset", TimeZone.getDefault().id)
        if (key.isNotEmpty() && keyHeader.isNotEmpty()) put(keyHeader, key)
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
