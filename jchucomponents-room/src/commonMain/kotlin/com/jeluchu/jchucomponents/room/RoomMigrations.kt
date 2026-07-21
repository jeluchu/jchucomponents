package com.jeluchu.jchucomponents.room

import androidx.room3.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

/**
 * Creates a Room 3 migration that executes [statements] in order.
 *
 * Each item must be one SQL statement. Room invokes the migration inside its own transaction, so
 * this helper deliberately does not start or commit a transaction.
 */
fun sqlMigration(
    startVersion: Int,
    endVersion: Int,
    vararg statements: String
) = Migration(startVersion, endVersion) { connection: SQLiteConnection ->
    statements.forEach(action = connection::execSQL)
}
