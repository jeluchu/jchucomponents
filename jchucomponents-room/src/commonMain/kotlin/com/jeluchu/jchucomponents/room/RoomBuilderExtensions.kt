package com.jeluchu.jchucomponents.room

import androidx.room3.RoomDatabase
import androidx.sqlite.SQLiteDriver
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlin.coroutines.CoroutineContext

/**
 * Default context used by [configureJchuRoom].
 *
 * This is `Dispatchers.IO` on Android. Kotlin/Native does not expose that
 * dispatcher, so Apple targets use `Dispatchers.Default` instead.
 */
expect val JchuRoomCoroutineContext: CoroutineContext

/**
 * Applies the standard JchuComponents Room 3 runtime configuration and returns this builder.
 *
 * The builder remains available to the caller, including through [configure], so app-specific
 * migrations, callbacks, journal mode, and connection-pool settings are never hidden. This
 * function does not enable destructive migrations.
 */
fun <T : RoomDatabase> RoomDatabase.Builder<T>.configureJchuRoom(
    driver: SQLiteDriver = BundledSQLiteDriver(),
    queryCoroutineContext: CoroutineContext = JchuRoomCoroutineContext,
    configure: RoomDatabase.Builder<T>.() -> Unit = {}
) = apply {
    setDriver(driver)
    setQueryCoroutineContext(queryCoroutineContext)
    configure()
}
