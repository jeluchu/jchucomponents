package com.jeluchu.jchucomponents.foundation.validators

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CreditCardValidatorTest {
    @Test
    fun detectsKnownCardTypes() {
        assertEquals(
            CreditCardValidator.CardType.Visa,
            CreditCardValidator.getCardType("4111 1111 1111 1111")
        )
        assertEquals(
            CreditCardValidator.CardType.MasterCard,
            CreditCardValidator.getCardType("5555-5555-5555-4444")
        )
    }

    @Test
    fun validatesCardsByKnownPattern() {
        assertTrue(CreditCardValidator.isValidCard("4111111111111111"))
        assertFalse(CreditCardValidator.isValidCard("123456"))
    }
}
