package com.jeluchu.jchucomponents.prefs

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * A stored preference exposed as a Kotlin property, so an app declares each variable in one line:
 *
 * ```
 * object PreferencesService {
 *     var isFirstLaunch by booleanPreference("first_boot", default = true)
 *     var theme by stringPreference("theme", default = "system")
 *     var sortMode by enumPreference("sort", SortMode.Name)
 *     val launches by intPreference("launches")      // `val` for read-only use
 * }
 * ```
 *
 * The helper is looked up on every access with [jchuPreferencesHelper], so the object can be created before
 * `JchuConfig` starts. The same caveat as [JchuPreferencesHelper] applies: a read made in the first milliseconds
 * after startup returns the default.
 */
fun booleanPreference(key: String, default: Boolean = false, helper: () -> JchuPreferencesHelper = ::jchuPreferencesHelper): ReadWriteProperty<Any?, Boolean> =
    JchuPreferenceProperty(helper, { it.getBoolean(key, default) }, { h, v -> h.saveBoolean(key, v) })

fun intPreference(key: String, default: Int = 0, helper: () -> JchuPreferencesHelper = ::jchuPreferencesHelper): ReadWriteProperty<Any?, Int> =
    JchuPreferenceProperty(helper, { it.getInt(key, default) }, { h, v -> h.saveInt(key, v) })

fun longPreference(key: String, default: Long = 0L, helper: () -> JchuPreferencesHelper = ::jchuPreferencesHelper): ReadWriteProperty<Any?, Long> =
    JchuPreferenceProperty(helper, { it.getLong(key, default) }, { h, v -> h.saveLong(key, v) })

fun floatPreference(key: String, default: Float = 0f, helper: () -> JchuPreferencesHelper = ::jchuPreferencesHelper): ReadWriteProperty<Any?, Float> =
    JchuPreferenceProperty(helper, { it.getFloat(key, default) }, { h, v -> h.saveFloat(key, v) })

fun doublePreference(key: String, default: Double = 0.0, helper: () -> JchuPreferencesHelper = ::jchuPreferencesHelper): ReadWriteProperty<Any?, Double> =
    JchuPreferenceProperty(helper, { it.getDouble(key, default) }, { h, v -> h.saveDouble(key, v) })

fun stringPreference(key: String, default: String = "", helper: () -> JchuPreferencesHelper = ::jchuPreferencesHelper): ReadWriteProperty<Any?, String> =
    JchuPreferenceProperty(helper, { it.getString(key, default) }, { h, v -> h.saveString(key, v) })

/** An enum stored by its name. An unknown or missing stored name gives [default]. */
inline fun <reified E : Enum<E>> enumPreference(
    key: String,
    default: E,
    noinline helper: () -> JchuPreferencesHelper = ::jchuPreferencesHelper
): ReadWriteProperty<Any?, E> =
    JchuPreferenceProperty(
        helper,
        { h -> h.getStringOrNull(key)?.let { name -> enumValues<E>().firstOrNull { it.name == name } } ?: default },
        { h, value -> h.saveString(key, value.name) }
    )

@PublishedApi
internal class JchuPreferenceProperty<T>(
    private val helper: () -> JchuPreferencesHelper,
    private val read: (JchuPreferencesHelper) -> T,
    private val write: (JchuPreferencesHelper, T) -> Unit
) : ReadWriteProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T = read(helper())

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = write(helper(), value)
}
