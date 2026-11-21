package com.jeluchu.jchucomponents.network.http

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.Logger

internal actual fun createPlatformHttpClient(configuration: HttpClientConfiguration): HttpClient =
    when (configuration.engine) {
        JchuHttpEngine.Cio ->
            HttpClient(engineFactory = CIO) {
                applyJchuConfiguration(configuration, platformLogger = Logger.ANDROID)
            }

        JchuHttpEngine.Default ->
            HttpClient(engineFactory = Android) {
                applyJchuConfiguration(configuration, platformLogger = Logger.ANDROID)
            }
    }
