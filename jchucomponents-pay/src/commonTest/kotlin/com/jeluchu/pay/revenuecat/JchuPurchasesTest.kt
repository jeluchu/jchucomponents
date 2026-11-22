package com.jeluchu.pay.revenuecat

import com.jeluchu.pay.revenuecat.models.JchuCustomer
import com.jeluchu.pay.revenuecat.models.JchuEntitlement
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class JchuPurchasesTest {
    @Test
    fun userIdIsTrimmedAndBlankMeansNoUser() {
        assertEquals("user-1", normalizeUserId("  user-1 "))
        assertNull(normalizeUserId("   "))
        assertNull(normalizeUserId(""))
        assertNull(normalizeUserId(null))
    }

    @Test
    fun customerReportsOnlyActiveEntitlements() {
        val customer =
            JchuCustomer(
                appUserId = "user-1",
                entitlements =
                    mapOf(
                        "plus" to entitlement("plus", isActive = true, product = "plus_annual"),
                        "pro" to entitlement("pro", isActive = false, product = "pro_monthly")
                    )
            )

        assertTrue(customer.isActive("plus"))
        assertFalse(customer.isActive("pro"))
        assertFalse(customer.isActive("missing"))
        assertEquals("plus_annual", customer.activeProduct("plus"))
        assertNull(customer.activeProduct("pro"))
        assertNull(customer.activeProduct("missing"))
    }

    private fun entitlement(
        id: String,
        isActive: Boolean,
        product: String
    ) = JchuEntitlement(
        identifier = id,
        isActive = isActive,
        productIdentifier = product,
        expirationMillis = null,
        willRenew = isActive
    )
}
