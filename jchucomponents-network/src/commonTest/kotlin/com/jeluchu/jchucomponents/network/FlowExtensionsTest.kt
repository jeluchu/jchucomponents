package com.jeluchu.jchucomponents.network

import com.jeluchu.jchucomponents.network.extensions.flow.flowCollector
import com.jeluchu.jchucomponents.network.extensions.flow.flowResourceCollector
import com.jeluchu.jchucomponents.network.models.Failure
import com.jeluchu.jchucomponents.network.models.Resource
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class FlowExtensionsTest {
    @Test
    fun flowCollectorCollectsEveryValue() =
        runBlocking {
            val values = mutableListOf<Int>()
            val collected = CompletableDeferred<Unit>()

            flowOf(1, 2, 3).flowCollector(this) {
                values += it
                if (it == 3) collected.complete(Unit)
            }
            collected.await()

            assertEquals(listOf(1, 2, 3), values)
        }

    @Test
    fun flowResourceCollectorDispatchesEveryResourceState() =
        runBlocking {
            val scope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)
            val events = mutableListOf<String>()
            val failure = Failure.CustomError(errorMessage = "Failed")

            flowOf(
                Resource.Loading(),
                Resource.Success("Loaded"),
                Resource.Error<Failure, String>(failure),
            ).flowResourceCollector(
                scope = scope,
                initialValue = Resource.Loading(),
                onLoading = { events += "loading" },
                onSuccess = { events += "success:$it" },
                onFailure = { events += "failure:${it?.message}" },
            )

            assertEquals(
                listOf("loading", "loading", "success:Loaded", "failure:Failed"),
                events,
            )
            scope.cancel()
        }
}
