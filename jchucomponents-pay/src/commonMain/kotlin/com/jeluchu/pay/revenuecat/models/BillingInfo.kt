package com.jeluchu.pay.revenuecat.models

data class BillingInfo(
    val info: SubscriptionInfo,
    val packages: List<RevenueCatPackage>,
    val products: List<Product>
) {
    companion object {
        fun empty() = BillingInfo(
            packages = emptyList(),
            products = emptyList(),
            info = SubscriptionInfo.empty()
        )
    }
}

expect class RevenueCatPackage
