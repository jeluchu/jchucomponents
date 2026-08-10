package com.jeluchu.jchucomponents.supabase.auth

import com.jeluchu.jchucomponents.network.models.Resource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.io.IOException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

class JchuSupabaseAuthFailureTest {
    @Test
    fun authResourceMapsNetworkFailuresAfterLoading() = runBlocking {
        val resources =
            supabaseAuthResource<Unit> {
                throw IOException("Network unavailable")
            }.toList()

        assertIs<Resource.Loading>(resources[0])
        val error = assertIs<Resource.Error<JchuSupabaseAuthFailure, Unit>>(resources[1])
        assertEquals(expected = JchuSupabaseAuthFailureCode.NETWORK, actual = error.error?.code)
        assertTrue(error.error?.retryable == true)
    }

    @Test
    fun authResourceDoesNotSwallowCancellation() {
        assertFailsWith<CancellationException> {
            runBlocking {
                supabaseAuthResource<Unit> {
                    throw CancellationException("cancelled")
                }.collect()
            }
        }
    }
}
