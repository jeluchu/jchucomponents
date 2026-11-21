package com.jeluchu.jchucomponents.network.http

import com.jeluchu.jchucomponents.config.JchuConfig
import io.ktor.client.HttpClient
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.named
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotSame
import kotlin.test.assertNull

class JchuNetworkBlockTest {
    @AfterTest
    fun tearDown() = stopKoin()

    @Test
    fun theDefaultAndNamedClientsAreSeparateKoinDefinitions() {
        val koin =
            JchuConfig {
                network { baseUrl = "https://api.example.com/" }
                network("downloads") { useJson = false }
                network("preview") { followRedirects = false }
                koin()
            }.koin!!

        val default = koin.get<HttpClient>()
        val downloads = koin.get<HttpClient>(named("downloads"))
        val preview = koin.get<HttpClient>(named("preview"))

        assertNotSame(default, downloads)
        assertNotSame(downloads, preview)
        assertNotSame(default, preview)
    }

    @Test
    fun theSameNameTwiceIsRejectedAndBlankNamesToo() {
        assertFailsWith<IllegalArgumentException> {
            JchuConfig {
                network("a")
                network("a")
            }
        }
        assertFailsWith<IllegalArgumentException> { JchuConfig { network("  ") } }
    }

    @Test
    fun blockOptionsReachTheConfiguration() {
        val configuration =
            JchuNetworkBlock()
                .apply {
                    followRedirects = false
                    useJson = false
                    engine = JchuHttpEngine.Cio
                    allowCellularAccess = false
                }.toConfiguration()

        assertFalse(configuration.followRedirects)
        assertFalse(configuration.useJson)
        assertEquals(JchuHttpEngine.Cio, configuration.engine)
        assertFalse(configuration.allowCellularAccess)
        assertNull(configuration.headerProvider)
    }

    @Test
    fun dynamicHeadersAreReadAtEachCallAndBlankOnesAreOmitted() {
        var key = "first"
        val provider =
            JchuNetworkBlock()
                .apply {
                    header("x-api-key") { key }
                    header("x-empty") { "" }
                }.toConfiguration()
                .headerProvider!!

        assertEquals(mapOf("x-api-key" to "first"), provider())
        key = "second"
        assertEquals(mapOf("x-api-key" to "second"), provider())
        key = ""
        assertEquals(emptyMap(), provider())
    }

    @Test
    fun aBlankHeaderNameIsRejected() {
        assertFailsWith<IllegalArgumentException> { JchuNetworkBlock().header(" ") { "x" } }
    }
}
