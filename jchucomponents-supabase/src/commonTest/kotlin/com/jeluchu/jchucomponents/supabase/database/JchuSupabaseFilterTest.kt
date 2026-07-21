package com.jeluchu.jchucomponents.supabase.database

import kotlin.test.Test
import kotlin.test.assertFailsWith

class JchuSupabaseFilterTest {
    @Test
    fun emptyFilterListsAreRejected() {
        assertFailsWith<IllegalArgumentException> {
            requireSupabaseFilters(emptyList())
        }
    }

    @Test
    fun nonEmptyFilterListsAreAccepted() {
        requireSupabaseFilters(
            listOf(JchuSupabaseFilter.eq(column = "id", value = "row-id"))
        )
    }
}
