package com.jeluchu.jchucomponents.supabase.di

import com.jeluchu.jchucomponents.config.JchuConfigBuilder
import com.jeluchu.jchucomponents.config.JchuFeature
import com.jeluchu.jchucomponents.config.JchuPlatform
import com.jeluchu.jchucomponents.supabase.JchuSupabaseAuthConfig
import com.jeluchu.jchucomponents.supabase.JchuSupabaseConfig
import com.jeluchu.jchucomponents.supabase.JchuSupabaseModules
import org.koin.core.module.Module

/** Options of the `supabase { }` block of `JchuConfig`. */
class JchuSupabaseBlock {
    var url: String = ""
    var publishableKey: String = ""
    var auth: JchuSupabaseAuthConfig = JchuSupabaseAuthConfig()
    var modules: JchuSupabaseModules = JchuSupabaseModules()

    /**
     * Creates the client when Koin starts instead of on first use, so the stored session restores at
     * launch. On by default.
     */
    var createdAtStart: Boolean = true

    /**
     * When `true`, a missing or unsafe configuration (blank values, `YOUR_` placeholders, secret or
     * `service_role` keys, non-HTTPS urls) skips the feature instead of throwing. Use it for apps that
     * can run, showing a "not configured" state, without a backend. Check with [isJchuSupabaseConfigured].
     */
    var optional: Boolean = false
}

/**
 * Registers the Supabase client and its enabled sub-clients (auth, database, functions, realtime).
 *
 * ```
 * JchuConfig(application) {
 *     supabase {
 *         url = getString(R.string.supabase_url)
 *         publishableKey = getString(R.string.supabase_publishable_key)
 *     }
 * }
 * ```
 */
fun JchuConfigBuilder.supabase(block: JchuSupabaseBlock.() -> Unit) {
    val options = JchuSupabaseBlock().apply(block)
    val url = options.url.trim()
    val publishableKey = options.publishableKey.trim()
    jchuSupabaseConfigProblem(url, publishableKey)?.let { problem ->
        require(options.optional) { "JchuConfig: supabase $problem." }
        return
    }
    install(
        JchuSupabaseFeature(
            JchuSupabaseConfig(
                url = url,
                publishableKey = publishableKey,
                auth = options.auth,
                modules = options.modules
            ),
            options.createdAtStart
        )
    )
}

internal class JchuSupabaseFeature(
    private val config: JchuSupabaseConfig,
    private val createdAtStart: Boolean = false
) : JchuFeature {
    override val id: String = ID

    override fun modules(platform: JchuPlatform): List<Module> = listOf(jchuSupabaseModule(config, createdAtStart))

    companion object {
        const val ID = "supabase"
    }
}
