package com.jeluchu.pay.revenuecat.models

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

/** A purchasable package of an offering with the store data a paywall shows. */
data class JchuPackage(
    val identifier: String,
    val productIdentifier: String,
    val title: String,
    val description: String,
    /** Price as the store formats it for the user's country and currency. */
    val formattedPrice: String,
    /** Price in millionths of the currency unit, for comparing plans or computing savings. */
    val priceMicros: Long,
    /** RevenueCat package type name, for example `MONTHLY`, `ANNUAL` or `CUSTOM`. */
    val type: String
)
