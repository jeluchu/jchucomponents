package com.jeluchu.jchucomponents.prefs

import android.content.Context
import com.jeluchu.jchucomponents.config.JchuPlatform
import com.jeluchu.jchucomponents.config.androidContext

private fun sharedPreferences(platform: JchuPlatform, store: JchuLegacyStore) =
    platform.androidContext.let { context ->
        context.getSharedPreferences(store.name ?: "${context.packageName}_preferences", Context.MODE_PRIVATE)
    }

internal actual fun readLegacyValues(platform: JchuPlatform, store: JchuLegacyStore): Map<String, Any> =
    sharedPreferences(platform, store).all
        .filterKeys { it in store.keys }
        .mapNotNull { (key, value) -> value?.takeIf { it is Boolean || it is Int || it is Long || it is Float || it is String }?.let { key to it } }
        .toMap()

internal actual fun removeLegacyValues(platform: JchuPlatform, store: JchuLegacyStore) {
    sharedPreferences(platform, store).edit().apply { store.keys.forEach(::remove) }.apply()
}
