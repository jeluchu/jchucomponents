package com.jeluchu.jchucomponents.room

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RoomMigrationExecutionTest {
    @Test
    fun migrationExecutesEveryStatementInOrder() = runBlocking {
        val connection = BundledSQLiteDriver().open(":memory:")
        try {
            sqlMigration(
                startVersion = 1,
                endVersion = 2,
                "CREATE TABLE example (id INTEGER PRIMARY KEY, label TEXT NOT NULL)",
                "INSERT INTO example (id, label) VALUES (1, 'migrated')"
            ).migrate(connection)

            val statement = connection.prepare("SELECT id, label FROM example")
            try {
                assertTrue(statement.step())
                assertEquals(expected = 1L, actual = statement.getLong(0))
                assertEquals(expected = "migrated", actual = statement.getText(1))
            } finally {
                statement.close()
            }
        } finally {
            connection.close()
        }
    }
}
