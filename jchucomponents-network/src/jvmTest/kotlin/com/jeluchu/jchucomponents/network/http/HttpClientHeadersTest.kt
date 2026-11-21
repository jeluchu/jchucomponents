package com.jeluchu.jchucomponents.network.http

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Headers
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class HttpClientHeadersTest {
    private fun clientFor(
        configuration: HttpClientConfiguration,
        seen: MutableList<Headers>
    ) = HttpClient(
        MockEngine { request ->
            seen += request.headers
            respond("{}", HttpStatusCode.OK)
        }
    ) {
        applyJchuConfiguration(configuration, Logger.SIMPLE)
    }

    @Test
    fun theProviderIsEvaluatedForEveryRequest() =
        runBlocking {
            var key = "one"
            val seen = mutableListOf<Headers>()
            val client =
                clientFor(
                    HttpClientConfiguration(
                        baseUrl = "https://example.com/",
                        headerProvider = { mapOf("x-api-key" to key) }
                    ),
                    seen
                )

            client.get("ping")
            key = "two"
            client.get("ping")

            assertEquals(listOf("one", "two"), seen.map { it["x-api-key"] })
            client.close()
        }

    @Test
    fun aDynamicHeaderReplacesTheStaticOneWithTheSameName() =
        runBlocking {
            val seen = mutableListOf<Headers>()
            val client =
                clientFor(
                    HttpClientConfiguration(
                        baseUrl = "https://example.com/",
                        defaultHeaders = mapOf("x-api-key" to "static", "x-other" to "kept"),
                        headerProvider = { mapOf("x-api-key" to "dynamic") }
                    ),
                    seen
                )

            client.get("ping")

            assertEquals(listOf("dynamic"), seen.single().getAll("x-api-key"))
            assertEquals("kept", seen.single()["x-other"])
            client.close()
        }

    @Test
    fun jsonHeadersAreSentOnlyWhenJsonIsOn() =
        runBlocking {
            val withJson = mutableListOf<Headers>()
            val withoutJson = mutableListOf<Headers>()
            val jsonClient = clientFor(HttpClientConfiguration(baseUrl = "https://example.com/"), withJson)
            val rawClient = clientFor(HttpClientConfiguration(baseUrl = "https://example.com/", useJson = false), withoutJson)

            jsonClient.get("ping")
            rawClient.get("ping")

            assertEquals("application/json", withJson.single()[HttpHeaders.Accept])
            assertNotEquals("application/json", withoutJson.single()[HttpHeaders.Accept])
            jsonClient.close()
            rawClient.close()
        }
}
