package com.jeluchu.pay.revenuecat.kmp.models

import com.revenuecat.purchases.kmp.models.Package

data class BillingInfo(
    val info: SubscriptionInfo,
    val packages: List<Package>,
    val products: List<Product>
) {
    companion object {
        fun empty() =
            BillingInfo(
                info = SubscriptionInfo.empty(),
                packages = emptyList(),
                products = emptyList()
            )
    }
}
