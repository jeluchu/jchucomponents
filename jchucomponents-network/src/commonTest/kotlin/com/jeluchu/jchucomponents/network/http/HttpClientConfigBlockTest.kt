package com.jeluchu.jchucomponents.network.http

import com.jeluchu.jchucomponents.config.JchuConfig
import io.ktor.client.HttpClient
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class HttpClientConfigBlockTest {
    @AfterTest
    fun tearDown() = stopKoin()

    @Test
    fun networkBlockProvidesAnHttpClientThroughKoin() {
        val result =
            JchuConfig {
                network { enableLogging = false }
                koin()
            }

        assertNotNull(requireNotNull(result.koinApplication).koin.get<HttpClient>())
    }
}
