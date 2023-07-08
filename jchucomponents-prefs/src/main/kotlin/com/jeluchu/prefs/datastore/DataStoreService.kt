package com.jeluchu.prefs.datastore

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

class DataStoreService {
    private val preferences by lazy { DataStoreHelpers() }

    fun <T> savePreference(
        tag: Preferences.Key<T>,
        value: T
    ) = preferences.savePreference(tag, value)

    fun savePreference(
        tag: Preferences.Key<Int>,
        value: Int
    ) = preferences.savePreference(tag, value)

    fun savePreference(
        tag: Preferences.Key<Long>,
        value: Long
    ) = preferences.savePreference(tag, value)

    fun savePreference(
        tag: Preferences.Key<String>,
        value: String
    ) = preferences.savePreference(tag, value)

    fun savePreference(
        tag: Preferences.Key<Boolean>,
        value: Boolean
    ) = preferences.savePreference(tag, value)

    fun <T> getPreference(
        tag: Preferences.Key<T>
    ) = preferences.getPreference(tag)

    fun getPreference(
        tag: Preferences.Key<Int>,
        default: Int
    ) = preferences.getPreference(tag, default)

    fun getPreference(
        tag: Preferences.Key<Long>,
        default: Long
    ) = preferences.getPreference(tag, default)

    fun getPreference(
        tag: Preferences.Key<String>,
        default: String
    ) = preferences.getPreference(tag, default)

    fun getPreference(
        tag: Preferences.Key<Boolean>,
        default: Boolean
    ) = preferences.getPreference(tag, default)

    companion object {
        fun intPrefsKey(name: String) = intPreferencesKey(name)
        fun floatPrefsKey(name: String) = floatPreferencesKey(name)
        fun longPrefsKey(name: String) = longPreferencesKey(name)
        fun doublePrefsKey(name: String) = doublePreferencesKey(name)
        fun stringPrefsKey(name: String) = stringPreferencesKey(name)
        fun booleanPrefsKey(name: String) = booleanPreferencesKey(name)
    }
}