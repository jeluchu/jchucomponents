package com.jeluchu.jchucomponents.prefs

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

object JchuPreferenceKeys {
    fun int(name: String): Preferences.Key<Int> = intPreferencesKey(name)

    fun long(name: String): Preferences.Key<Long> = longPreferencesKey(name)

    fun float(name: String): Preferences.Key<Float> = floatPreferencesKey(name)

    fun double(name: String): Preferences.Key<Double> = doublePreferencesKey(name)

    fun string(name: String): Preferences.Key<String> = stringPreferencesKey(name)

    fun boolean(name: String): Preferences.Key<Boolean> = booleanPreferencesKey(name)

    fun stringSet(name: String): Preferences.Key<Set<String>> = stringSetPreferencesKey(name)
}
