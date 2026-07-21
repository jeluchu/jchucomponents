package com.jeluchu.jchucomponents.supabase.flow

import com.jeluchu.jchucomponents.network.models.Resource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

class JchuSupabaseFlowTest {
    @Test
    fun resourceEmitsLoadingBeforeSuccess() = runBlocking {
        val resources = supabaseResource { "ready" }.toList()

        assertIs<Resource.Loading>(resources[0])
        assertEquals(expected = "ready", actual = resources[1].data)
    }

    @Test
    fun resourceMapsFailuresToErrors() = runBlocking {
        val resources =
            supabaseResource<Unit> {
                error("Supabase failed")
            }.toList()

        assertIs<Resource.Loading>(resources[0])
        assertIs<Resource.Error<*, *>>(resources[1])
        assertEquals(expected = "Supabase failed", actual = resources[1].error?.message)
    }

    @Test
    fun resourceDoesNotSwallowCancellation() {
        assertFailsWith<CancellationException> {
            runBlocking {
                supabaseResource<Unit> {
                    throw CancellationException("cancelled")
                }.collect()
            }
        }
    }
}
