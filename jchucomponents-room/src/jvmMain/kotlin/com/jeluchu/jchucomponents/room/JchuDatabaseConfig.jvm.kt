package com.jeluchu.jchucomponents.room

import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.jeluchu.jchucomponents.config.JchuConfigBuilder
import org.koin.dsl.module
import org.koin.dsl.onClose
import java.io.File

@PublishedApi
internal fun jchuDatabasePath(name: String): String {
    val directory = File(System.getProperty("user.home"), ".jchucomponents")
    directory.mkdirs()
    return File(directory, name).absolutePath
}

/**
 * Registers the Room database [T] stored as [name] under `<user home>/.jchucomponents`.
 * Call it once per database; each one is injectable with Koin.
 */
inline fun <reified T : RoomDatabase> JchuConfigBuilder.database(
    name: String,
    noinline factory: (() -> T)? = null,
    noinline block: JchuDatabaseBlock<T>.() -> Unit = {}
) {
    val options = JchuDatabaseBlock<T>().apply(block)
    installDatabase(name) {
        module {
            single<T> {
                val path = jchuDatabasePath(name)
                val builder =
                    if (factory == null) Room.databaseBuilder<T>(path) else Room.databaseBuilder<T>(path, factory)
                options.applyTo(builder).build()
            } onClose { it?.close() }
        }
    }
}
