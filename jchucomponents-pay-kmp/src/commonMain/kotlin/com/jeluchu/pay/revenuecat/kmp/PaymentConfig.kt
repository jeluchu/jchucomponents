package com.jeluchu.pay.revenuecat.kmp

import com.jeluchu.jchucomponents.config.JchuConfigBuilder
import com.jeluchu.jchucomponents.config.JchuFeature
import com.jeluchu.jchucomponents.config.JchuPlatform

/** Options of the `payments { }` block of `JchuConfig`. */
class JchuPaymentsBlock {
    /** RevenueCat public API key of the current store. */
    var apiKey: String = ""
    var isDebug: Boolean = false

    /** Name of the entitlement that unlocks the premium features. */
    var subscriptionName: String = ""

    /**
     * When true, a blank or placeholder [apiKey] (`$(KEY)` or `YOUR_...` left by an unconfigured build) skips
     * RevenueCat instead of failing, so debug builds and forks without store keys still start.
     * Check `Purchases.isConfigured` before using purchases.
     */
    var optional: Boolean = false
}

/**
 * Configures RevenueCat through [initPayment].
 *
 * ```
 * JchuConfig(application) {
 *     payments {
 *         apiKey = getString(R.string.revenuecat_android_api_key)
 *         subscriptionName = "premium"
 *     }
 * }
 * ```
 */
fun JchuConfigBuilder.payments(block: JchuPaymentsBlock.() -> Unit) {
    val options = JchuPaymentsBlock().apply(block)
    if (options.optional && jchuPaymentsKeyProblem(options.apiKey) != null) return
    require(options.apiKey.isNotBlank()) { "JchuConfig: payments.apiKey cannot be blank." }
    install(JchuPaymentsFeature(options.apiKey, options.isDebug, options.subscriptionName))
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
