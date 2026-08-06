package com.jeluchu.jchucomponents.prefs

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSRecursiveLock
import platform.Foundation.NSUserDomainMask

private val preferencesByPath = mutableMapOf<String, JchuPreferences>()
private val preferencesByPathLock = NSRecursiveLock()

@OptIn(ExperimentalForeignApi::class)
fun createJchuPreferences(fileName: String = JchuPreferencesFileName): JchuPreferences {
    val documentDirectory =
        NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )
    val path = requireNotNull(documentDirectory?.path) + "/$fileName"
    preferencesByPathLock.lock()
    return try {
        preferencesByPath.getOrPut(path) {
            JchuPreferences(dataStore = createJchuPreferencesDataStore(path))
        }
    } finally {
        preferencesByPathLock.unlock()
    }
}
