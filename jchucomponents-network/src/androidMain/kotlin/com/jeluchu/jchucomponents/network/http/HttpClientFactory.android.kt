package com.jeluchu.jchucomponents.network.http

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.Logger

internal actual fun createPlatformHttpClient(configuration: HttpClientConfiguration): HttpClient =
    HttpClient(engineFactory = Android) {
        applyJchuConfiguration(configuration, platformLogger = Logger.ANDROID)
    }
