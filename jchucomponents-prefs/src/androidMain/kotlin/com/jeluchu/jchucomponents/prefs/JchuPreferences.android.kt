package com.jeluchu.jchucomponents.prefs

import android.content.Context

fun createJchuPreferences(
    context: Context,
    fileName: String = JchuPreferencesFileName
): JchuPreferences {
    val path = context.applicationContext.filesDir.resolve(fileName).absolutePath
    return JchuPreferences(dataStore = createJchuPreferencesDataStore(path))
}
