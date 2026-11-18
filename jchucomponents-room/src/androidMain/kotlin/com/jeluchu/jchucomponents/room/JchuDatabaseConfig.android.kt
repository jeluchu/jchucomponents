package com.jeluchu.jchucomponents.room

import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.jeluchu.jchucomponents.config.JchuConfigBuilder
import com.jeluchu.jchucomponents.config.androidContext
import org.koin.dsl.module
import org.koin.dsl.onClose

/**
 * Registers the Room database [T] stored as [name] in the app's databases folder.
 * Call it once per database; each one is injectable with Koin.
 *
 * ```
 * JchuConfig(application) {
 *     database<CacheDatabase>("app-cache.db") { fallbackToDestructiveMigration = true }
 * }
 * ```
 */
inline fun <reified T : RoomDatabase> JchuConfigBuilder.database(
    name: String,
    noinline factory: (() -> T)? = null,
    noinline block: JchuDatabaseBlock<T>.() -> Unit = {}
) {
    val context = platform.androidContext
    val options = JchuDatabaseBlock<T>().apply(block)
    installDatabase(name) {
        module {
            single<T> {
                val builder =
                    if (factory == null) {
                        Room.databaseBuilder<T>(context, name)
                    } else {
                        Room.databaseBuilder<T>(context, name, factory)
                    }
                options.applyTo(builder).build()
            } onClose { it?.close() }
        }
    }
}
