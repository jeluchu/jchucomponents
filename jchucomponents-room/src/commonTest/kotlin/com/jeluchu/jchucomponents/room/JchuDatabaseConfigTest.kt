package com.jeluchu.jchucomponents.room

import com.jeluchu.jchucomponents.config.JchuConfig
import org.koin.dsl.module
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class JchuDatabaseConfigTest {
    @Test
    fun eachDatabaseContributesItsOwnModule() {
        val result =
            JchuConfig {
                installDatabase("cache.db") { module { } }
                installDatabase("favourites.db") { module { } }
            }

        assertEquals(2, result.modules.size)
    }

    @Test
    fun sameDatabaseNameTwiceIsRejected() {
        assertFailsWith<IllegalArgumentException> {
            JchuConfig {
                installDatabase("cache.db") { module { } }
                installDatabase("cache.db") { module { } }
            }
        }
    }

    @Test
    fun blankNameIsRejected() {
        assertFailsWith<IllegalArgumentException> { JchuConfig { installDatabase(" ") { module { } } } }
    }
}
