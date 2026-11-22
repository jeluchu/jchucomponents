package com.jeluchu.jchucomponents.foundation.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class RunCatchingCancellableTest {
    @Test
    fun returnsTheValueOfASuccessfulBlock() {
        assertEquals(5, runCatchingCancellable { 2 + 3 }.getOrNull())
    }

    @Test
    fun capturesOrdinaryExceptionsAndErrors() {
        val failure = IllegalStateException("boom")
        assertEquals(failure, runCatchingCancellable { throw failure }.exceptionOrNull())
        assertIs<AssertionError>(runCatchingCancellable { throw AssertionError("fail") }.exceptionOrNull())
    }

    @Test
    fun rethrowsCancellationExceptions() {
        assertFailsWith<CancellationException> {
            runCatchingCancellable { throw CancellationException("stop") }
        }
    }

    @Test
    fun worksAroundSuspendCalls() =
        runBlocking<Unit> {
            assertEquals(7, runCatchingCancellable { delay(1); 7 }.getOrNull())
        }

    @Test
    fun cancellationStopsTheCoroutineWhereRunCatchingWouldNot() =
        runBlocking<Unit> {
            var reachedAfterCatching = false
            var reachedAfterPlainRunCatching = false

            val safe = launch(start = CoroutineStart.UNDISPATCHED) {
                runCatchingCancellable { awaitCancellation() }
                reachedAfterCatching = true
            }
            val unsafe = launch(start = CoroutineStart.UNDISPATCHED) {
                runCatching { awaitCancellation() }
                reachedAfterPlainRunCatching = true
            }
            safe.cancelAndJoin()
            unsafe.cancelAndJoin()

            assertFalse(reachedAfterCatching)
            assertTrue(reachedAfterPlainRunCatching)
        }

    @Test
    fun receiverVersionBehavesTheSame() {
        assertEquals(6, "abc".runCatchingCancellable { length * 2 }.getOrNull())
        assertFailsWith<CancellationException> {
            "abc".runCatchingCancellable { throw CancellationException("stop") }
        }
    }

    @Test
    fun exceptionsVariantCapturesExceptionsButLetsErrorsThrough() {
        val failure = IllegalStateException("boom")
        assertEquals(failure, runCatchingExceptions { throw failure }.exceptionOrNull())
        assertEquals(3, runCatchingExceptions { 3 }.getOrNull())
        assertFailsWith<AssertionError> { runCatchingExceptions { throw AssertionError("fatal") } }
    }

    @Test
    fun exceptionsVariantRethrowsCancellation() {
        assertFailsWith<CancellationException> { runCatchingExceptions { throw CancellationException("stop") } }
        assertFailsWith<CancellationException> { "abc".runCatchingExceptions { throw CancellationException("stop") } }
        assertEquals(3, "abc".runCatchingExceptions { length }.getOrNull())
    }

    @Test
    fun exceptionsVariantStopsACancelledCoroutine() =
        runBlocking<Unit> {
            var reached = false
            val job = launch(start = CoroutineStart.UNDISPATCHED) {
                runCatchingExceptions { awaitCancellation() }
                reached = true
            }
            job.cancelAndJoin()
            assertFalse(reached)
        }
}
