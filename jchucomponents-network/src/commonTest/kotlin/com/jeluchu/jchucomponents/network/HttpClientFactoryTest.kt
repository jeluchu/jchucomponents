package com.jeluchu.jchucomponents.network

import com.jeluchu.jchucomponents.network.http.HttpClientConfiguration
import com.jeluchu.jchucomponents.network.http.createHttpClient
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HttpClientFactoryTest {
    @Test
    fun exposesReusableDefaults() {
        val configuration = HttpClientConfiguration()

        assertEquals(90_000, configuration.requestTimeoutMillis)
        assertTrue(configuration.enableCache)
        assertTrue(configuration.expectSuccess)
    }

    @Test
    fun createsAndClosesPlatformClient() {
        val client =
            createHttpClient(
                HttpClientConfiguration(
                    baseUrl = "https://example.com/api/",
                    enableLogging = false,
                ),
            )

        client.close()
    }
}
