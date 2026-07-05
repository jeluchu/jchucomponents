package com.jeluchu.pay.revenuecat

import com.jeluchu.pay.revenuecat.utils.PriceUtil
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PriceUtilTest {
    @Test
    fun dividesPricesWithPrefixAndSuffixCurrencies() {
        assertEquals("3,20 EUR", PriceUtil.toDividedPrice("12,80 EUR", 4))
        assertEquals("$3,20", PriceUtil.toDividedPrice("$12.80", 4))
    }

    @Test
    fun multipliesPricesAndKeepsCurrencyPlacement() {
        assertEquals("12,80 EUR", PriceUtil.toFullPrice("3,20 EUR", 4))
        assertEquals("$12,80", PriceUtil.toFullPrice("$3.20", 4))
    }

    @Test
    fun returnsNullForPricesWithoutNumbers() {
        assertNull(PriceUtil.toDividedPrice("EUR", 4))
        assertNull(PriceUtil.toFullPrice("EUR", 4))
    }
}
