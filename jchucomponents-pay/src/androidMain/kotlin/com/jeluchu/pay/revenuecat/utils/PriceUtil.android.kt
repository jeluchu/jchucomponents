package com.jeluchu.pay.revenuecat.utils

import android.util.Log
import com.android.billingclient.api.ProductDetails
import com.jeluchu.pay.revenuecat.extensions.isInAppPurchase
import com.jeluchu.pay.revenuecat.extensions.isSubscription

/**
 * A helper method making it easier to retrieve a formatted price from product details.
 * Parameters only apply to subscription products.
 *
 * @param subscriptionOfferIndex index of [ProductDetails.getSubscriptionOfferDetails].
 * @param subscriptionPricingPhaseIndex index of [ProductDetails.SubscriptionOfferDetails.getPricingPhases]
 * @return formatted price or null on error
 */
fun ProductDetails.getFormattedPrice(
    subscriptionOfferIndex: Int = 0,
    subscriptionPricingPhaseIndex: Int = 0
): String? =
    if (isInAppPurchase()) {
        oneTimePurchaseOfferDetails?.formattedPrice
    } else {
        try {
            if (isSubscription()) {
                subscriptionOfferDetails
                    ?.getOrNull(subscriptionOfferIndex)
                    ?.pricingPhases
                    ?.pricingPhaseList
                    ?.getOrNull(subscriptionPricingPhaseIndex)
                    ?.formattedPrice
            } else {
                null
            }
        } catch (e: Exception) {
            if (PriceUtil.enableLogging) Log.e(PriceUtil.TAG, e.message.orEmpty())
            null
        }
    }
