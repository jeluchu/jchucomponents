package com.jeluchu.jchucomponents.network

import com.jeluchu.jchucomponents.network.extensions.flow.ResourceException
import com.jeluchu.jchucomponents.network.extensions.flow.asResourceFlow
import com.jeluchu.jchucomponents.network.extensions.flow.awaitRequiredResourceData
import com.jeluchu.jchucomponents.network.extensions.flow.awaitResource
import com.jeluchu.jchucomponents.network.extensions.flow.awaitResourceData
import com.jeluchu.jchucomponents.network.extensions.flow.awaitResourceUnit
import com.jeluchu.jchucomponents.network.extensions.flow.catchAsResourceError
import com.jeluchu.jchucomponents.network.models.Failure
import com.jeluchu.jchucomponents.network.models.Resource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNull

class ResourceAwaitExtensionsTest {
    private val failure = Failure.CustomError(errorMessage = "Failed")

    private fun <T> resources(vararg items: Resource<Failure, T>): Flow<Resource<Failure, T>> = flowOf(*items)

    @Test
    fun awaitResourceSkipsLoadingPlaceholders() =
        runBlocking<Unit> {
            val result = resources(Resource.Loading(), Resource.Loading(), Resource.Success("done")).awaitResource()

            assertEquals("done", assertIs<Resource.Success<String>>(result).data)
        }

    @Test
    fun awaitResourceDataReturnsTheSuccessPayload() =
        runBlocking<Unit> {
            assertEquals(7, resources(Resource.Loading(), Resource.Success(7)).awaitResourceData())
        }

    @Test
    fun awaitResourceDataThrowsTheFailureOfAnError() =
        runBlocking<Unit> {
            val thrown =
                assertFailsWith<ResourceException> {
                    resources<Int>(Resource.Loading(), Resource.Error(failure)).awaitResourceData()
                }

            assertEquals(failure, thrown.failure)
            assertEquals("Failed", thrown.message)
        }

    @Test
    fun awaitRequiredResourceDataRejectsMissingData() =
        runBlocking<Unit> {
            assertNull(resources<String?>(Resource.Success(null)).awaitResourceData())
            assertFailsWith<ResourceException> {
                resources<String?>(Resource.Success(null)).awaitRequiredResourceData()
            }
        }

    @Test
    fun awaitResourceUnitPropagatesErrors() =
        runBlocking<Unit> {
            resources(Resource.Success(Unit)).awaitResourceUnit()
            assertFailsWith<ResourceException> {
                resources<Unit>(Resource.Error(failure)).awaitResourceUnit()
            }
        }

    @Test
    fun asResourceFlowEmitsLoadingThenSuccesses() =
        runBlocking<Unit> {
            val events = flowOf(1, 2).asResourceFlow().toList()

            assertIs<Resource.Loading>(events[0])
            assertEquals(listOf(1, 2), events.drop(1).map { assertIs<Resource.Success<Int>>(it).data })
        }

    @Test
    fun asResourceFlowTurnsUpstreamExceptionsIntoErrors() =
        runBlocking<Unit> {
            val events = flow<Int> { error("boom") }.asResourceFlow().toList()

            assertIs<Resource.Loading>(events[0])
            assertIs<Resource.Error<Failure, Int>>(events[1])
        }

    @Test
    fun cancellationIsNeverConvertedIntoAnError() =
        runBlocking<Unit> {
            assertFailsWith<CancellationException> {
                flow<Resource<Failure, Int>> { throw CancellationException("stop") }.catchAsResourceError().toList()
            }
        }

    @Test
    fun catchAsResourceErrorReportsOtherExceptions() =
        runBlocking<Unit> {
            val events = flow<Resource<Failure, Int>> { error("boom") }.catchAsResourceError().toList()

            assertIs<Resource.Error<Failure, Int>>(events.single())
        }
}
