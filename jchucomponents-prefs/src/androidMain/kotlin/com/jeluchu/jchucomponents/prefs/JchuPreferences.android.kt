package com.jeluchu.jchucomponents.prefs

import android.content.Context

fun createJchuPreferences(
    context: Context,
    fileName: String = JchuPreferencesFileName
): JchuPreferences {
    val preferencesFile = context.applicationContext.filesDir.resolve(relative = fileName)
    val path = preferencesFile.absolutePath
    return JchuPreferences(dataStore = createJchuPreferencesDataStore(path))
}
