package com.jeluchu.jchucomponents.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toPath

const val JchuPreferencesFileName: String = "jchucomponents.preferences_pb"

class JchuPreferences(
    val dataStore: DataStore<Preferences>
) {
    fun <T> observe(
        key: Preferences.Key<T>,
        defaultValue: T
    ): Flow<T> =
        dataStore.data.map { preferences ->
            preferences[key] ?: defaultValue
        }

    suspend fun <T> get(
        key: Preferences.Key<T>,
        defaultValue: T
    ): T = observe(key, defaultValue).first()

    suspend fun <T> set(
        key: Preferences.Key<T>,
        value: T
    ) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    suspend fun edit(transform: suspend (MutablePreferences) -> Unit): Preferences = dataStore.edit(transform)

    fun observeInt(
        key: String,
        defaultValue: Int = 0
    ): Flow<Int> = observe(JchuPreferenceKeys.int(key), defaultValue)

    suspend fun getInt(
        key: String,
        defaultValue: Int = 0
    ): Int = get(JchuPreferenceKeys.int(key), defaultValue)

    suspend fun setInt(
        key: String,
        value: Int
    ): Unit = set(JchuPreferenceKeys.int(key), value)

    fun observeLong(
        key: String,
        defaultValue: Long = 0L
    ): Flow<Long> = observe(JchuPreferenceKeys.long(key), defaultValue)

    suspend fun getLong(
        key: String,
        defaultValue: Long = 0L
    ): Long = get(JchuPreferenceKeys.long(key), defaultValue)

    suspend fun setLong(
        key: String,
        value: Long
    ): Unit = set(JchuPreferenceKeys.long(key), value)

    fun observeFloat(
        key: String,
        defaultValue: Float = 0f
    ): Flow<Float> = observe(JchuPreferenceKeys.float(key), defaultValue)

    suspend fun getFloat(
        key: String,
        defaultValue: Float = 0f
    ): Float = get(JchuPreferenceKeys.float(key), defaultValue)

    suspend fun setFloat(
        key: String,
        value: Float
    ): Unit = set(JchuPreferenceKeys.float(key), value)

    fun observeDouble(
        key: String,
        defaultValue: Double = 0.0
    ): Flow<Double> = observe(JchuPreferenceKeys.double(key), defaultValue)

    suspend fun getDouble(
        key: String,
        defaultValue: Double = 0.0
    ): Double = get(JchuPreferenceKeys.double(key), defaultValue)

    suspend fun setDouble(
        key: String,
        value: Double
    ): Unit = set(JchuPreferenceKeys.double(key), value)

    fun observeString(
        key: String,
        defaultValue: String = ""
    ): Flow<String> = observe(JchuPreferenceKeys.string(key), defaultValue)

    suspend fun getString(
        key: String,
        defaultValue: String = ""
    ): String = get(JchuPreferenceKeys.string(key), defaultValue)

    suspend fun setString(
        key: String,
        value: String
    ): Unit = set(JchuPreferenceKeys.string(key), value)

    fun observeBoolean(
        key: String,
        defaultValue: Boolean = false
    ): Flow<Boolean> = observe(JchuPreferenceKeys.boolean(key), defaultValue)

    suspend fun getBoolean(
        key: String,
        defaultValue: Boolean = false
    ): Boolean = get(JchuPreferenceKeys.boolean(key), defaultValue)

    suspend fun setBoolean(
        key: String,
        value: Boolean
    ): Unit = set(JchuPreferenceKeys.boolean(key), value)

    fun observeStringSet(
        key: String,
        defaultValue: Set<String> = emptySet()
    ): Flow<Set<String>> = observe(JchuPreferenceKeys.stringSet(key), defaultValue)

    suspend fun getStringSet(
        key: String,
        defaultValue: Set<String> = emptySet()
    ): Set<String> = get(JchuPreferenceKeys.stringSet(key), defaultValue)

    suspend fun setStringSet(
        key: String,
        value: Set<String>
    ): Unit = set(JchuPreferenceKeys.stringSet(key), value)

    fun contains(key: String): Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences.asMap().keys.any { it.name == key }
        }

    suspend fun remove(vararg keys: String) {
        dataStore.edit { preferences ->
            keys.forEach { key ->
                preferences.removeAllTypes(key)
            }
        }
    }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}

fun createJchuPreferencesDataStore(path: String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath {
        path.toPath()
    }

fun createJchuPreferences(dataStore: DataStore<Preferences>): JchuPreferences = JchuPreferences(dataStore)

private fun MutablePreferences.removeAllTypes(key: String) {
    remove(JchuPreferenceKeys.int(key))
    remove(JchuPreferenceKeys.long(key))
    remove(JchuPreferenceKeys.float(key))
    remove(JchuPreferenceKeys.double(key))
    remove(JchuPreferenceKeys.string(key))
    remove(JchuPreferenceKeys.boolean(key))
    remove(JchuPreferenceKeys.stringSet(key))
}
