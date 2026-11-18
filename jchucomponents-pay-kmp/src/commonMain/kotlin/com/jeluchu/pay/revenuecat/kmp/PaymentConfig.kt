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
