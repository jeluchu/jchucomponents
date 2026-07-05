package com.jeluchu.jchucomponents.prefs

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

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
    return JchuPreferences(dataStore = createJchuPreferencesDataStore(path))
}
