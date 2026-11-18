package com.jeluchu.jchucomponents.prefs

import com.jeluchu.jchucomponents.config.JchuConfigBuilder
import com.jeluchu.jchucomponents.config.JchuFeature
import com.jeluchu.jchucomponents.config.JchuPlatform
import org.koin.core.module.Module
import org.koin.dsl.module

/** Options of the `preferences { }` block of `JchuConfig`. */
class JchuPreferencesBlock {
    /** Name of the DataStore file. */
    var fileName: String = JchuPreferencesFileName
}

/**
 * Registers [JchuPreferences] (DataStore) so it can be injected with Koin.
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
    install(JchuPreferencesFeature(options.fileName))
}

internal class JchuPreferencesFeature(
    private val fileName: String
) : JchuFeature {
    override val id: String = ID

    override fun modules(platform: JchuPlatform): List<Module> =
        listOf(
            module {
                single<JchuPreferences> { createPlatformJchuPreferences(platform, fileName) }
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
