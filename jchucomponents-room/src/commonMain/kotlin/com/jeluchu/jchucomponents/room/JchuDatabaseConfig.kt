package com.jeluchu.jchucomponents.room

import androidx.room3.RoomDatabase
import androidx.room3.migration.Migration
import androidx.sqlite.SQLiteDriver
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.jeluchu.jchucomponents.config.JchuConfigBuilder
import com.jeluchu.jchucomponents.config.JchuFeature
import com.jeluchu.jchucomponents.config.JchuPlatform
import org.koin.core.module.Module

/** Options of the `database<T>(name) { }` block of `JchuConfig`. */
class JchuDatabaseBlock<T : RoomDatabase> {
    /** SQLite driver. The bundled driver is used by default on every platform. */
    var driver: SQLiteDriver = BundledSQLiteDriver()

    /** Recreates the database when no migration path exists. Off by default. */
    var fallbackToDestructiveMigration: Boolean = false

    private val migrationList = mutableListOf<Migration>()
    private var extra: RoomDatabase.Builder<T>.() -> Unit = {}

    fun migrations(vararg migrations: Migration) {
        migrationList += migrations
    }

    /** Escape hatch to the underlying Room builder (callbacks, journal mode...). */
    fun configure(block: RoomDatabase.Builder<T>.() -> Unit) {
        extra = block
    }

    @PublishedApi
    internal fun applyTo(builder: RoomDatabase.Builder<T>): RoomDatabase.Builder<T> =
        builder.configureJchuRoom(driver = driver) {
            if (migrationList.isNotEmpty()) addMigrations(*migrationList.toTypedArray())
            if (fallbackToDestructiveMigration) fallbackToDestructiveMigration()
            extra()
        }
}

@PublishedApi
internal fun JchuConfigBuilder.installDatabase(
    name: String,
    buildModule: () -> Module
) {
    require(name.isNotBlank()) { "JchuConfig: database name cannot be blank." }
    install(JchuDatabaseFeature(name, buildModule))
}

internal class JchuDatabaseFeature(
    name: String,
    private val buildModule: () -> Module
) : JchuFeature {
    override val id: String = "database:$name"

    override fun modules(platform: JchuPlatform): List<Module> = listOf(buildModule())
}
