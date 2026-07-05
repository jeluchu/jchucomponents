package com.jeluchu.jchucomponents.network

import com.jeluchu.jchucomponents.network.extensions.handleFailure
import com.jeluchu.jchucomponents.network.http.HttpStatusCode
import com.jeluchu.jchucomponents.network.http.getHttpErrorInfo
import com.jeluchu.jchucomponents.network.models.Failure
import com.jeluchu.jchucomponents.network.models.Resource
import com.jeluchu.jchucomponents.network.resource.mapToResource
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class NetworkResourceTest {
    @Test
    fun mapsHttpStatusCodeToReasonPhraseAndCategory() {
        val status = getHttpErrorInfo(404)

        assertEquals(HttpStatusCode.NotFound, status)
        assertEquals("Not Found", status.message)
        assertTrue(status.isClientError)
    }

    @Test
    fun serverFailureUsesHttpStatusWhenMessageIsBlank() {
        val failure = Failure.ServerError(errorCode = 503)

        assertEquals(503, failure.code)
        assertEquals("Service Unavailable", failure.message)
        assertEquals("HTTP 503: Service Unavailable", failure.handleFailure())
    }

    @Test
    fun mapToResourceKeepsCachedDataWhenFailureExists() =
        runBlocking {
            val failure = Failure.ServerError(errorCode = 500)
            val emissions =
                flowOf("cached")
                    .mapToResource(transform = { it.uppercase() }, failure = failure)
                    .toList()

            val error = assertIs<Resource.Error<Failure, String>>(emissions.single())
            assertEquals("CACHED", error.data)
            assertEquals(failure, error.error)
        }
}
