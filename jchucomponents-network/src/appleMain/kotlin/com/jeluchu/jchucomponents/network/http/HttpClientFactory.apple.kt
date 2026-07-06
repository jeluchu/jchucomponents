package com.jeluchu.jchucomponents.network.http

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.SIMPLE

internal actual fun createPlatformHttpClient(configuration: HttpClientConfiguration): HttpClient =
    HttpClient(engineFactory = Darwin) {
        applyJchuConfiguration(configuration, platformLogger = Logger.SIMPLE)
    }
