package com.jeluchu.pay.revenuecat.kmp.models

/** One RevenueCat entitlement of a customer. */
data class JchuEntitlement(
    val identifier: String,
    val isActive: Boolean,
    val productIdentifier: String,
    /** Expiry in epoch milliseconds, or null for lifetime entitlements. */
    val expirationMillis: Long?,
    val willRenew: Boolean
)

/** The RevenueCat customer, reduced to what apps need to decide what to unlock. */
data class JchuCustomer(
    val appUserId: String,
    val entitlements: Map<String, JchuEntitlement>
) {
    /** True when the entitlement [identifier] exists and is active. */
    fun isActive(identifier: String): Boolean = entitlements[identifier]?.isActive == true

    /** The product that grants the active entitlement [identifier], or null when it is not active. */
    fun activeProduct(identifier: String): String? =
        entitlements[identifier]?.takeIf { it.isActive }?.productIdentifier
}
