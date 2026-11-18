package com.jeluchu.jchucomponents.room

import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.jeluchu.jchucomponents.config.JchuConfigBuilder
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.dsl.module
import org.koin.dsl.onClose
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

/** Where an Apple database file is stored. */
enum class JchuDatabaseDirectory {
    /** The documents folder: backed up by the OS. Default. */
    Documents,

    /** The caches folder: not backed up and purgeable by the OS. Use it for caches. */
    Caches
}

@OptIn(ExperimentalForeignApi::class)
@PublishedApi
internal fun jchuDatabasePath(
    name: String,
    directory: JchuDatabaseDirectory
): String {
    val url =
        NSFileManager.defaultManager.URLForDirectory(
            directory = if (directory == JchuDatabaseDirectory.Caches) NSCachesDirectory else NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = true,
            error = null
        )
    return requireNotNull(url?.path) + "/$name"
}

/**
 * Registers the Room database [T] stored as [name] in [directory] (documents by default).
 * Kotlin/Native has no reflection, so [factory] must create the database, usually with the
 * generated constructor: `factory = { CacheDatabaseConstructor.initialize() }`.
 */
inline fun <reified T : RoomDatabase> JchuConfigBuilder.database(
    name: String,
    noinline factory: () -> T,
    directory: JchuDatabaseDirectory = JchuDatabaseDirectory.Documents,
    noinline block: JchuDatabaseBlock<T>.() -> Unit = {}
) {
    val options = JchuDatabaseBlock<T>().apply(block)
    installDatabase(name) {
        module {
            single<T> {
                options.applyTo(Room.databaseBuilder<T>(jchuDatabasePath(name, directory), factory)).build()
            } onClose { it?.close() }
        }
    }
}
