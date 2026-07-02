package com.jeluchu.jchucomponents.network

import com.jeluchu.jchucomponents.network.api.ApiEndpoint
import com.jeluchu.jchucomponents.network.api.ApiVersion
import com.jeluchu.jchucomponents.network.http.path
import kotlin.test.Test
import kotlin.test.assertEquals

class ApiEndpointTest {
    @Test
    fun buildsVersionedPath() {
        val endpoint = TestEndpoint(ApiVersion.V3, "catalog/items")

        assertEquals("v3/catalog/items.json", endpoint.path())
    }

    @Test
    fun omitsEmptyVersionPath() {
        val endpoint = TestEndpoint(ApiVersion.NONE, "health", format = "txt")

        assertEquals("health.txt", endpoint.path())
    }

    private class TestEndpoint(
        version: ApiVersion,
        override val endpoint: String,
        override val format: String = "json",
    ) : ApiEndpoint(version)
}
