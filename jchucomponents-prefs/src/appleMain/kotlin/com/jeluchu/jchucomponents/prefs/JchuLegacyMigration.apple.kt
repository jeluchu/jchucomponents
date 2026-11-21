@file:OptIn(ExperimentalForeignApi::class)

package com.jeluchu.jchucomponents.prefs

import com.jeluchu.jchucomponents.config.JchuPlatform
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.Foundation.NSNumber
import platform.Foundation.NSUserDefaults

private fun defaults(store: JchuLegacyStore): NSUserDefaults =
    store.name?.let { NSUserDefaults(suiteName = it) } ?: NSUserDefaults.standardUserDefaults

internal actual fun readLegacyValues(platform: JchuPlatform, store: JchuLegacyStore): Map<String, Any> {
    val defaults = defaults(store)
    return store.keys.mapNotNull { key ->
        val value = defaults.objectForKey(key) ?: return@mapNotNull null
        legacyValue(value)?.let { key to it }
    }.toMap()
}

internal actual fun removeLegacyValues(platform: JchuPlatform, store: JchuLegacyStore) {
    val defaults = defaults(store)
    store.keys.forEach { defaults.removeObjectForKey(it) }
}

/** Maps what `UserDefaults` returns (NSString, NSNumber) to a Kotlin value, using the number's stored C type. */
private fun legacyValue(value: Any): Any? =
    when (value) {
        is String -> value
        is NSNumber ->
            when (value.objCType?.toKString()) {
                "c", "B" -> value.boolValue
                "d" -> value.doubleValue
                "f" -> value.floatValue
                "i", "s" -> value.intValue
                else -> value.longLongValue
            }
        else -> null
    }
