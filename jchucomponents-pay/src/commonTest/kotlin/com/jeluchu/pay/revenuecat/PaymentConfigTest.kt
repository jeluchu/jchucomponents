package com.jeluchu.pay.revenuecat

import com.jeluchu.jchucomponents.config.JchuConfig
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class PaymentConfigTest {
    @Test
    fun blankApiKeyIsRejected() {
        assertFailsWith<IllegalArgumentException> { JchuConfig { payments { subscriptionName = "premium" } } }
    }

    @Test
    fun optionalSkipsBlankAndPlaceholderKeys() {
        assertEquals(0, JchuConfig { payments { optional = true } }.modules.size)
        JchuConfig { payments { apiKey = "\$(REVENUECAT)"; optional = true } }
        JchuConfig { payments { apiKey = "YOUR_KEY"; optional = true } }
    }

    @Test
    fun keyProblemsAreDetected() {
        assertEquals("blank", jchuPaymentsKeyProblem(" "))
        assertEquals("placeholder", jchuPaymentsKeyProblem("\$(KEY)"))
        assertNull(jchuPaymentsKeyProblem("goog_abc"))
    }
}
