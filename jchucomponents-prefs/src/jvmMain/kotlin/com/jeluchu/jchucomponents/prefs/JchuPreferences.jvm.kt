package com.jeluchu.jchucomponents.prefs

import java.io.File

private val preferencesByPath = mutableMapOf<String, JchuPreferences>()

/** Default location for desktop preferences: `<user home>/.jchucomponents`. */
private fun defaultPreferencesDirectory(): File = File(System.getProperty("user.home"), ".jchucomponents")

/**
 * Creates (or returns the cached) preferences stored in [fileName] inside [directory].
 *
 * Instances are cached by absolute path so DataStore never has two active instances for one file.
 */
fun createJchuPreferences(
    fileName: String = JchuPreferencesFileName,
    directory: File = defaultPreferencesDirectory()
): JchuPreferences = synchronized(preferencesByPath) {
    directory.mkdirs()
    val path = File(directory, fileName).absolutePath

    preferencesByPath.getOrPut(path) {
        JchuPreferences(dataStore = createJchuPreferencesDataStore(path))
    }
}
