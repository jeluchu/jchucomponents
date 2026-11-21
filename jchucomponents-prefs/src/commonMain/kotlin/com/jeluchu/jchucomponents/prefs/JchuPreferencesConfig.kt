package com.jeluchu.jchucomponents.prefs

import com.jeluchu.jchucomponents.config.JchuConfigBuilder
import com.jeluchu.jchucomponents.config.JchuFeature
import com.jeluchu.jchucomponents.config.JchuPlatform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.module.Module
import org.koin.dsl.module

/** Options of the `preferences { }` block of `JchuConfig`. */
class JchuPreferencesBlock {
    /** Name of the DataStore file. */
    var fileName: String = JchuPreferencesFileName

    internal val legacyStores = mutableListOf<JchuLegacyStore>()

    internal fun addLegacyStore(store: JchuLegacyStore) {
        legacyStores += store
    }
}

/**
 * Registers [JchuPreferences] (DataStore) and the synchronous [JchuPreferencesHelper] so they can be
 * injected with Koin (or read with `jchuPreferencesHelper()`).
 *
 * ```
 * JchuConfig(application) {
 *     preferences { fileName = "app_preferences.preferences_pb" }
 * }
 * ```
 */
fun JchuConfigBuilder.preferences(block: JchuPreferencesBlock.() -> Unit = {}) {
    val options = JchuPreferencesBlock().apply(block)
    require(options.fileName.isNotBlank()) { "JchuConfig: preferences.fileName cannot be blank." }
    install(JchuPreferencesFeature(options.fileName, options.legacyStores.toList()))
}

internal class JchuPreferencesFeature(
    private val fileName: String,
    private val legacyStores: List<JchuLegacyStore> = emptyList()
) : JchuFeature {
    override val id: String = ID

    override fun initialize(platform: JchuPlatform) {
        if (legacyStores.isEmpty()) return
        val prefs = createPlatformJchuPreferences(platform, fileName)
        CoroutineScope(SupervisorJob() + Dispatchers.Default).launch {
            runLegacyMigrations(platform, prefs, legacyStores)
        }
    }

    override fun modules(platform: JchuPlatform): List<Module> =
        listOf(
            module {
                single<JchuPreferences> { createPlatformJchuPreferences(platform, fileName) }
                single<JchuPreferencesHelper> { createJchuPreferencesHelper(get()) }
            }
        )

    companion object {
        const val ID = "preferences"
    }
}

internal expect fun createPlatformJchuPreferences(
    platform: JchuPlatform,
    fileName: String
): JchuPreferences
