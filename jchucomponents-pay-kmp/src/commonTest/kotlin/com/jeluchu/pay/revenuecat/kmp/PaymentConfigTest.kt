package com.jeluchu.pay.revenuecat.kmp

import com.jeluchu.jchucomponents.config.JchuConfig
import kotlin.test.Test
import kotlin.test.assertFailsWith

class PaymentConfigTest {
    @Test
    fun blankApiKeyIsRejected() {
        assertFailsWith<IllegalArgumentException> { JchuConfig { payments { subscriptionName = "premium" } } }
    }
}
