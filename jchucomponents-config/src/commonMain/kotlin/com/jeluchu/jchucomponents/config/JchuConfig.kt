package com.jeluchu.jchucomponents.config

import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module

/**
 * Configures every JchuComponents feature declared in [block] and, optionally, starts Koin.
 *
 * ```
 * JchuConfig(platform) {
 *     preferences { fileName = "app_preferences" }
 *     supabase { url = "..."; publishableKey = "..." }
 *     koin { modules(appModule) }
 * }
 * ```
 *
 * Only the declared blocks are initialized. Without a `koin { }` block nothing is started: use
 * [JchuConfigResult.modules] with your own `startKoin`.
 */
@Suppress("FunctionName")
fun JchuConfig(
    platform: JchuPlatform = JchuPlatform.None,
    block: JchuConfigBuilder.() -> Unit
): JchuConfigResult {
    attachJchuAppInfo(platform)
    return JchuConfigBuilder(platform).apply(block).build()
}

/**
 * Like [JchuConfig], but safe to call more than once: the first call configures and starts everything, later
 * calls ignore [block] and return the same result. Meant for entry points the host can reach repeatedly (an iOS
 * `App.init`, a SwiftUI preview, a desktop `main`). Call it from one thread, usually the main one.
 */
@Suppress("FunctionName")
fun JchuConfigOnce(
    platform: JchuPlatform = JchuPlatform.None,
    block: JchuConfigBuilder.() -> Unit
): JchuConfigResult {
    startedOnce?.let { return it }
    return JchuConfig(platform, block).also { startedOnce = it }
}

/** Forgets the result of [JchuConfigOnce]. Meant for tests. */
fun resetJchuConfigOnce() {
    startedOnce = null
}

private var startedOnce: JchuConfigResult? = null

/** Receiver of the [JchuConfig] block. Feature modules add their blocks as extensions of it. */
class JchuConfigBuilder internal constructor(
    val platform: JchuPlatform
) {
    private val features = LinkedHashMap<String, JchuFeature>()
    private var koinBlock: (KoinApplication.() -> Unit)? = null

    /** Installs a [feature]. Library blocks call this; installing the same id twice is an error. */
    fun install(feature: JchuFeature) {
        require(features.put(feature.id, feature) == null) {
            "JchuConfig: '${feature.id}' is configured more than once."
        }
    }

    /**
     * Starts Koin with the modules of every installed feature plus the ones added in [block].
     * On Android the application context is registered automatically.
     */
    fun koin(block: KoinApplication.() -> Unit = {}) {
        check(koinBlock == null) { "JchuConfig: koin { } is declared more than once." }
        koinBlock = block
    }

    internal fun build(): JchuConfigResult {
        val ordered = sortByDependencies(features)
        ordered.forEach { feature ->
            try {
                feature.initialize(platform)
            } catch (error: Throwable) {
                throw IllegalStateException(
                    "JchuConfig: failed to initialize '${feature.id}': ${error.message}",
                    error
                )
            }
        }
        val modules = ordered.flatMap { it.modules(platform) }
        val application =
            koinBlock?.let { extra ->
                startKoin {
                    applyJchuPlatform(platform)
                    modules(modules)
                    extra()
                }
            }
        ordered.forEach { feature ->
            try {
                feature.onStarted(platform, application?.koin)
            } catch (error: Throwable) {
                throw IllegalStateException(
                    "JchuConfig: failed to start '${feature.id}': ${error.message}",
                    error
                )
            }
        }
        return JchuConfigResult(modules = modules, koinApplication = application)
    }
}

/**
 * Outcome of [JchuConfig].
 *
 * @property modules Koin modules contributed by the installed features.
 * @property koinApplication The started Koin application, or `null` when no `koin { }` block was used.
 */
class JchuConfigResult internal constructor(
    val modules: List<Module>,
    val koinApplication: KoinApplication?
) {
    /** The started Koin, or `null` when no `koin { }` block was used. */
    val koin: Koin? get() = koinApplication?.koin
}

internal fun sortByDependencies(features: Map<String, JchuFeature>): List<JchuFeature> {
    features.values.forEach { feature ->
        feature.dependsOn.firstOrNull { it !in features }?.let { missing ->
            error("JchuConfig: '${feature.id}' requires '$missing' to be configured as well.")
        }
    }

    val ordered = ArrayList<JchuFeature>(features.size)
    val done = HashSet<String>()
    val visiting = HashSet<String>()

    fun visit(feature: JchuFeature) {
        if (feature.id in done) return
        check(visiting.add(feature.id)) { "JchuConfig: dependency cycle involving '${feature.id}'." }
        feature.dependsOn.forEach { visit(features.getValue(it)) }
        visiting.remove(feature.id)
        done.add(feature.id)
        ordered.add(feature)
    }

    features.values.forEach(::visit)
    return ordered
}

internal expect fun KoinApplication.applyJchuPlatform(platform: JchuPlatform)

/** Gives [JchuAppInfo] what it needs from [platform] (the Android context). */
internal expect fun attachJchuAppInfo(platform: JchuPlatform)
