package com.jeluchu.jchucomponents.prefs

import android.content.Context

private val preferencesByPath = mutableMapOf<String, JchuPreferences>()

fun createJchuPreferences(
    context: Context,
    fileName: String = JchuPreferencesFileName
): JchuPreferences = synchronized(preferencesByPath) {
    val preferencesFile = context.applicationContext.filesDir.resolve(fileName)
    val path = preferencesFile.absolutePath

    preferencesByPath.getOrPut(path) {
        JchuPreferences(dataStore = createJchuPreferencesDataStore(path))
    }
}
