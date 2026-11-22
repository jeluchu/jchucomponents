package com.jeluchu.pay.revenuecat

import com.jeluchu.jchucomponents.config.JchuAppInfo
import com.jeluchu.jchucomponents.config.JchuConfigBuilder
import com.jeluchu.jchucomponents.config.JchuFeature
import com.jeluchu.jchucomponents.config.JchuPlatform

/** Options of the `payments { }` block of `JchuConfig`. */
class JchuPaymentsBlock {
    /**
     * RevenueCat public API key of the current store. When left blank it is read from the app metadata entry
     * [API_KEY_METADATA] (manifest `<meta-data>` on Android, `Info.plist` on Apple), see [JchuAppInfo.metadata].
     */
    var apiKey: String = ""

    /** Verbose RevenueCat logs. Defaults to [JchuAppInfo.isDebug]. */
    var isDebug: Boolean = JchuAppInfo.isDebug

    /** Name of the entitlement that unlocks the premium features. */
    var subscriptionName: String = ""

    /**
     * When true, a blank or placeholder [apiKey] (`$(KEY)` or `YOUR_...` left by an unconfigured build) skips
     * RevenueCat instead of failing, so debug builds and forks without store keys still start.
     * Check `Purchases.isConfigured` before using purchases.
     */
    var optional: Boolean = false

    companion object {
        /** Name of the app metadata entry that holds the RevenueCat key when [apiKey] is not set. */
        const val API_KEY_METADATA: String = "revenuecat"
    }
}

/**
 * Configures RevenueCat through [initPayment].
 *
 * ```
 * JchuConfig(application) {
 *     payments {
 *         subscriptionName = "premium" // apiKey and isDebug come from the app metadata and build type
 *     }
 * }
 * ```
 */
fun JchuConfigBuilder.payments(block: JchuPaymentsBlock.() -> Unit) {
    val options = JchuPaymentsBlock().apply(block)
    val apiKey = options.apiKey.ifBlank { JchuAppInfo.metadata(JchuPaymentsBlock.API_KEY_METADATA) }
    if (options.optional && jchuPaymentsKeyProblem(apiKey) != null) return
    require(apiKey.isNotBlank()) { "JchuConfig: payments.apiKey cannot be blank." }
    install(JchuPaymentsFeature(apiKey, options.isDebug, options.subscriptionName))
}

internal class JchuPaymentsFeature(
    private val apiKey: String,
    private val isDebug: Boolean,
    private val subscriptionName: String
) : JchuFeature {
    override val id: String = ID

    override fun initialize(platform: JchuPlatform) {
        initPayment(apiKey = apiKey, isDebug = isDebug, subscriptionName = subscriptionName)
    }

    companion object {
        const val ID = "payments"
    }
}

/** Why [apiKey] cannot be used (blank or an unreplaced placeholder), or null when it looks usable. */
internal fun jchuPaymentsKeyProblem(apiKey: String): String? {
    val key = apiKey.trim()
    return when {
        key.isEmpty() -> "blank"
        key.startsWith("$(") || key.startsWith("YOUR_") -> "placeholder"
        else -> null
    }
}
