package com.jeluchu.jchucomponents.prefs

import android.content.Context
import com.jeluchu.jchucomponents.config.androidContext

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

internal actual fun createPlatformJchuPreferences(
    platform: com.jeluchu.jchucomponents.config.JchuPlatform,
    fileName: String
): JchuPreferences = createJchuPreferences(platform.androidContext, fileName)
