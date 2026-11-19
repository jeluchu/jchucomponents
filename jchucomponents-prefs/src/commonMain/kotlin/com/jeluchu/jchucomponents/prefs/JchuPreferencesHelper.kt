package com.jeluchu.jchucomponents.prefs

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform

/**
 * Synchronous access to the preferences configured with `preferences { }`.
 *
 * It sits on top of [JchuPreferences] (DataStore): the stored values are collected into an in-memory
 * cache in the background, reads are served from that cache, and writes update it immediately and are
 * persisted to DataStore in order on a background coroutine. Apps only declare their own variables:
 *
 * ```
 * object PreferencesService {
 *     private val helper = jchuPreferencesHelper()
 *
 *     val isFirstLaunch: Boolean
 *         get() = helper.getBoolean("first_boot", true)
 *
 *     fun savePreference(tag: String, value: Int) = helper.saveInt(tag, value)
 * }
 * ```
 *
 * Nothing blocks: the cache starts empty, so a read made before DataStore has delivered the file
 * (the first milliseconds after the helper is created) returns the default value. Use [JchuPreferences]
 * directly when you need `Flow` or `suspend` access.
 */
interface JchuPreferencesHelper {
    /** Increments every time any value changes, so UI can re-read what it shows (for example in Compose). */
    val changes: StateFlow<Int>

    fun saveInt(key: String, value: Int)
    fun saveLong(key: String, value: Long)
    fun saveFloat(key: String, value: Float)
    fun saveDouble(key: String, value: Double)
    fun saveString(key: String, value: String)
    fun saveBoolean(key: String, value: Boolean)

    fun getInt(key: String, default: Int = 0): Int
    fun getLong(key: String, default: Long = 0L): Long
    fun getFloat(key: String, default: Float = 0f): Float
    fun getDouble(key: String, default: Double = 0.0): Double
    fun getString(key: String, default: String = ""): String
    fun getBoolean(key: String, default: Boolean = false): Boolean

    /** The stored String, or null when the key is not set. */
    fun getStringOrNull(key: String): String?

    /** The stored value with its stored type, or null when the key is not set. */
    fun getAny(key: String): Any?

    fun contains(key: String): Boolean
    fun remove(key: String)
    fun clearAll()

    /** Suspends until every write made so far has been persisted to DataStore (for example before a restore finishes). */
    suspend fun flush()
}

/** Emits [key] whenever it changes (and its initial value). */
fun JchuPreferencesHelper.observeBoolean(key: String, default: Boolean = false): Flow<Boolean> =
    changes.map { getBoolean(key, default) }.distinctUntilChanged()

fun JchuPreferencesHelper.observeString(key: String, default: String = ""): Flow<String> =
    changes.map { getString(key, default) }.distinctUntilChanged()

fun JchuPreferencesHelper.observeInt(key: String, default: Int = 0): Flow<Int> =
    changes.map { getInt(key, default) }.distinctUntilChanged()

fun JchuPreferencesHelper.savePreference(tag: String, value: Int) = saveInt(tag, value)
fun JchuPreferencesHelper.savePreference(tag: String, value: Long) = saveLong(tag, value)
fun JchuPreferencesHelper.savePreference(tag: String, value: Float) = saveFloat(tag, value)
fun JchuPreferencesHelper.savePreference(tag: String, value: Double) = saveDouble(tag, value)
fun JchuPreferencesHelper.savePreference(tag: String, value: String) = saveString(tag, value)
fun JchuPreferencesHelper.savePreference(tag: String, value: Boolean) = saveBoolean(tag, value)

/** Reads [key] with the type of [default] (Int, Long, Float, Double, String or Boolean). */
@Suppress("UNCHECKED_CAST")
fun <T : Any> JchuPreferencesHelper.getPreference(key: String, default: T): T =
    when (default) {
        is Int -> getInt(key, default)
        is Long -> getLong(key, default)
        is Float -> getFloat(key, default)
        is Double -> getDouble(key, default)
        is String -> getString(key, default)
        is Boolean -> getBoolean(key, default)
        else -> throw IllegalArgumentException("Unsupported preference type: ${default::class}")
    } as T

/**
 * The helper registered by `preferences { }` in `JchuConfig` (resolved from Koin).
 *
 * When Koin has not been started at all (IDE previews, plain unit tests) an in-memory helper is returned, so UI
 * code that reads preferences keeps working there. If Koin is running but `preferences { }` was not declared this
 * throws, because that is a configuration mistake.
 */
fun jchuPreferencesHelper(): JchuPreferencesHelper =
    KoinPlatform.getKoinOrNull()?.get<JchuPreferencesHelper>() ?: previewHelper

private val previewHelper: JchuPreferencesHelper by lazy { createInMemoryJchuPreferencesHelper() }

/** A volatile helper that stores values in memory only. Useful for tests and previews. */
fun createInMemoryJchuPreferencesHelper(): JchuPreferencesHelper = InMemoryJchuPreferencesHelper()

private class InMemoryJchuPreferencesHelper : JchuPreferencesHelper {
    private val values = mutableMapOf<String, Any>()
    private val version = MutableStateFlow(0)

    override val changes: StateFlow<Int> = version.asStateFlow()

    private fun touch() = version.update { it + 1 }

    @Suppress("UNCHECKED_CAST")
    private fun <T> read(key: String, default: T): T = values[key] as? T ?: default

    override fun saveInt(key: String, value: Int) { values[key] = value; touch() }
    override fun saveLong(key: String, value: Long) { values[key] = value; touch() }
    override fun saveFloat(key: String, value: Float) { values[key] = value; touch() }
    override fun saveDouble(key: String, value: Double) { values[key] = value; touch() }
    override fun saveString(key: String, value: String) { values[key] = value; touch() }
    override fun saveBoolean(key: String, value: Boolean) { values[key] = value; touch() }

    override fun getInt(key: String, default: Int) = read(key, default)
    override fun getLong(key: String, default: Long) = read(key, default)
    override fun getFloat(key: String, default: Float) = read(key, default)
    override fun getDouble(key: String, default: Double) = read(key, default)
    override fun getString(key: String, default: String) = read(key, default)
    override fun getBoolean(key: String, default: Boolean) = read(key, default)
    override fun getStringOrNull(key: String): String? = values[key] as? String
    override fun getAny(key: String): Any? = values[key]
    override fun contains(key: String) = key in values
    override fun remove(key: String) { values.remove(key); touch() }
    override fun clearAll() { values.clear(); touch() }
    override suspend fun flush() = Unit
}

/** Creates a helper over [preferences]. Prefer `preferences { }` + [jchuPreferencesHelper]. */
fun createJchuPreferencesHelper(preferences: JchuPreferences): JchuPreferencesHelper =
    DataStoreJchuPreferencesHelper(preferences)

@OptIn(ExperimentalCoroutinesApi::class)
private class DataStoreJchuPreferencesHelper(
    private val preferences: JchuPreferences
) : JchuPreferencesHelper {
    private object Removed

    private val stored = MutableStateFlow<Map<String, Any>>(emptyMap())
    private val pending = MutableStateFlow<Map<String, Any>>(emptyMap())
    private val version = MutableStateFlow(0)
    private val writer = CoroutineScope(SupervisorJob() + Dispatchers.Default.limitedParallelism(1))

    override val changes: StateFlow<Int> = version.asStateFlow()

    init {
        // Nothing blocks: reads made before the first snapshot arrives return the default value.
        writer.launch {
            runCatching {
                preferences.dataStore.data.collect { snapshot ->
                    val values = snapshot.asMap().mapKeys { it.key.name }
                    stored.value = values
                    // A staged write is dropped once DataStore reports it.
                    pending.update { staged ->
                        staged.filter { (key, value) ->
                            if (value === Removed) values.containsKey(key) else values[key] != value
                        }
                    }
                    version.update { it + 1 }
                }
            }
        }
    }

    override fun saveInt(key: String, value: Int) = write(key, value) { preferences.setInt(key, value) }
    override fun saveLong(key: String, value: Long) = write(key, value) { preferences.setLong(key, value) }
    override fun saveFloat(key: String, value: Float) = write(key, value) { preferences.setFloat(key, value) }
    override fun saveDouble(key: String, value: Double) = write(key, value) { preferences.setDouble(key, value) }
    override fun saveString(key: String, value: String) = write(key, value) { preferences.setString(key, value) }
    override fun saveBoolean(key: String, value: Boolean) = write(key, value) { preferences.setBoolean(key, value) }

    // Reads tolerate a value saved with another numeric type (an Int read as Long, and so on).
    override fun getInt(key: String, default: Int): Int = (raw(key) as? Number)?.toInt() ?: default
    override fun getLong(key: String, default: Long): Long = (raw(key) as? Number)?.toLong() ?: default
    override fun getFloat(key: String, default: Float): Float = (raw(key) as? Number)?.toFloat() ?: default
    override fun getDouble(key: String, default: Double): Double = (raw(key) as? Number)?.toDouble() ?: default
    override fun getString(key: String, default: String): String =
        raw(key)?.let { it as? String ?: it.toString() } ?: default
    override fun getBoolean(key: String, default: Boolean): Boolean = raw(key) as? Boolean ?: default
    override fun getStringOrNull(key: String): String? = raw(key)?.let { it as? String ?: it.toString() }
    override fun getAny(key: String): Any? = raw(key)

    override fun contains(key: String): Boolean = raw(key) != null

    override fun remove(key: String) {
        pending.update { it + (key to Removed) }
        version.update { it + 1 }
        writer.launch { runCatching { preferences.remove(key) } }
    }

    override suspend fun flush() {
        writer.launch { }.join()
    }

    override fun clearAll() {
        pending.value = emptyMap()
        stored.value = emptyMap()
        version.update { it + 1 }
        writer.launch { runCatching { preferences.clear() } }
    }

    private fun raw(key: String): Any? {
        val staged = pending.value[key]
        if (staged != null) return staged.takeUnless { it === Removed }
        return stored.value[key]
    }

    private fun write(key: String, value: Any, persist: suspend () -> Unit) {
        pending.update { it + (key to value) }
        version.update { it + 1 }
        writer.launch { runCatching { persist() } }
    }
}
