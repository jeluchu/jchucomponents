package com.jeluchu.jchucomponents.room

import kotlin.test.Test
import kotlin.test.assertEquals

class RoomMigrationsTest {
    @Test
    fun migrationKeepsItsDeclaredVersionRange() {
        val migration = sqlMigration(
            startVersion = 3,
            endVersion = 5,
            "CREATE TABLE example (id INTEGER PRIMARY KEY)"
        )

        assertEquals(expected = 3, actual = migration.startVersion)
        assertEquals(expected = 5, actual = migration.endVersion)
    }
}
