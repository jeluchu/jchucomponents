package com.jeluchu.jchucomponents.prefs

import com.jeluchu.jchucomponents.config.JchuPlatform

// Desktop apps never had a native key-value store to migrate from.
internal actual fun readLegacyValues(platform: JchuPlatform, store: JchuLegacyStore): Map<String, Any> = emptyMap()

internal actual fun removeLegacyValues(platform: JchuPlatform, store: JchuLegacyStore) = Unit
