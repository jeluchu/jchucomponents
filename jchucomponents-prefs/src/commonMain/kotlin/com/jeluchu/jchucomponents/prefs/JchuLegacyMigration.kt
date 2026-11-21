package com.jeluchu.jchucomponents.prefs

import com.jeluchu.jchucomponents.config.JchuPlatform

/**
 * A native key-value store an app used before `preferences { }`: `SharedPreferences` on Android and `UserDefaults`
 * on Apple. See [migrateFromLegacy].
 *
 * @property name Android: the `SharedPreferences` file name. Apple: the `UserDefaults` suite name, or null for the
 * standard defaults. Ignored on JVM.
 * @property keys The keys to copy. Required because Apple's standard defaults also hold system entries.
 * @property removeAfter Delete the copied keys from the old store once they are safely in DataStore.
 */
class JchuLegacyStore(
    val name: String?,
    val keys: Set<String>,
    val removeAfter: Boolean
) {
    internal val markerKey: String = "jchu_legacy_migrated_${name ?: "standard"}"
}

/**
 * Copies [keys] from a native store into the DataStore of `preferences { }` the first time the app runs with
 * this block, so users keep their settings when an app moves to JchuComponents. Values that already exist in
 * DataStore are never overwritten, and a marker makes later launches skip the copy. Supported values are
 * Boolean, Int, Long, Float, Double and String; anything else is ignored.
 *
 * ```
 * preferences {
 *     fileName = "app.preferences_pb"
 *     migrateFromLegacy(name = "akaito_preferences", keys = setOf("theme", "first_boot"))
 * }
 * ```
 *
 * It runs in the background right after `JchuConfig`, so a read in the first milliseconds can still see the
 * default, as with any value of [JchuPreferencesHelper].
 */
fun JchuPreferencesBlock.migrateFromLegacy(
    name: String? = null,
    keys: Set<String>,
    removeAfter: Boolean = false
) {
    require(keys.isNotEmpty()) { "JchuConfig: migrateFromLegacy needs at least one key." }
    addLegacyStore(JchuLegacyStore(name, keys, removeAfter))
}

/** The legacy values currently stored for [store], by key. Never throws. */
internal expect fun readLegacyValues(platform: JchuPlatform, store: JchuLegacyStore): Map<String, Any>

internal expect fun removeLegacyValues(platform: JchuPlatform, store: JchuLegacyStore)

/**
 * Writes [values] into [prefs] unless the marker says it was done before. Existing keys win. Returns true
 * when the copy happened in this call.
 */
internal suspend fun copyLegacyValues(
    prefs: JchuPreferences,
    markerKey: String,
    values: Map<String, Any>
): Boolean {
    var copied = false
    prefs.edit { stored ->
        val marker = JchuPreferenceKeys.boolean(markerKey)
        if (stored[marker] == true) return@edit
        val existing = stored.asMap().keys.map { it.name }.toSet()
        values.forEach { (name, value) ->
            if (name !in existing) stored.putLegacy(name, value)
        }
        stored[marker] = true
        copied = true
    }
    return copied
}

private fun androidx.datastore.preferences.core.MutablePreferences.putLegacy(name: String, value: Any) {
    when (value) {
        is Boolean -> this[JchuPreferenceKeys.boolean(name)] = value
        is Int -> this[JchuPreferenceKeys.int(name)] = value
        is Long -> this[JchuPreferenceKeys.long(name)] = value
        is Float -> this[JchuPreferenceKeys.float(name)] = value
        is Double -> this[JchuPreferenceKeys.double(name)] = value
        is String -> this[JchuPreferenceKeys.string(name)] = value
    }
}

/** Runs every declared legacy copy. Failures are swallowed: a broken old store must not stop the app. */
internal suspend fun runLegacyMigrations(
    platform: JchuPlatform,
    prefs: JchuPreferences,
    stores: List<JchuLegacyStore>
) {
    stores.forEach { store ->
        runCatching {
            val values = readLegacyValues(platform, store)
            copyLegacyValues(prefs, store.markerKey, values)
            if (store.removeAfter) removeLegacyValues(platform, store)
        }
    }
}

