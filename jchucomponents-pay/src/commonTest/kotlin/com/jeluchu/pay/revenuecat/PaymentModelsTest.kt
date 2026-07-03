package com.jeluchu.pay.revenuecat

import com.jeluchu.pay.revenuecat.models.BillingInfo
import com.jeluchu.pay.revenuecat.models.SubscriptionInfo
import com.jeluchu.pay.revenuecat.models.SubscriptionState
import com.jeluchu.pay.revenuecat.models.SubscriptionsType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PaymentModelsTest {

    @Test
    fun emptySubscriptionHasNoActiveEntitlement() {
        val subscription = SubscriptionInfo.empty()

        assertEquals(SubscriptionsType.NONE, subscription.renewalType)
        assertEquals(SubscriptionState.NONE, subscription.state)
        assertEquals("", subscription.expireDate)
        assertFalse(subscription.promotional)
        assertNull(subscription.managementUrl)
    }

    @Test
    fun emptyBillingInfoContainsNoProducts() {
        val billingInfo = BillingInfo.empty()

        assertTrue(billingInfo.packages.isEmpty())
        assertTrue(billingInfo.products.isEmpty())
        assertEquals(SubscriptionInfo.empty(), billingInfo.info)
    }
}
