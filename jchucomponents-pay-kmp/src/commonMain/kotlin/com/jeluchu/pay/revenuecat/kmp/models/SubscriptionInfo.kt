package com.jeluchu.pay.revenuecat.kmp.models

data class SubscriptionInfo(
    val renewalType: SubscriptionsType,
    val expireDate: String,
    val promotional: Boolean,
    val state: SubscriptionState,
    val managementUrl: String?
) {
    companion object {
        fun empty() =
            SubscriptionInfo(
                renewalType = SubscriptionsType.NONE,
                expireDate = "",
                promotional = false,
                state = SubscriptionState.NONE,
                managementUrl = null
            )
    }
}
