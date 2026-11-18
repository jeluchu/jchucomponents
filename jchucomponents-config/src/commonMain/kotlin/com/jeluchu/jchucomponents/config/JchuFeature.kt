package com.jeluchu.jchucomponents.config

import org.koin.core.module.Module

/**
 * A piece of JchuComponents that can be configured through [JchuConfig].
 *
 * Each library module (prefs, supabase, payments...) ships its own feature plus a
 * `JchuConfigBuilder.xxx { }` extension that installs it, so an app only sees (and depends on)
 * the blocks of the modules it actually uses.
 */
interface JchuFeature {
    /** Unique id of the feature, used to detect duplicates and to express [dependsOn]. */
    val id: String

    /** Ids of features that must be installed, and initialized, before this one. */
    val dependsOn: Set<String> get() = emptySet()

    /** Eager initialization (SDK setup, global singletons...). Called once, in dependency order. */
    fun initialize(platform: JchuPlatform) = Unit

    /** Koin modules this feature contributes. Called once, in dependency order. */
    fun modules(platform: JchuPlatform): List<Module> = emptyList()
}
