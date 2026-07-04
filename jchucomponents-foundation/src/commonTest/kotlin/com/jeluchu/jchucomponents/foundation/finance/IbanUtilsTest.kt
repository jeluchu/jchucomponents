package com.jeluchu.jchucomponents.foundation.finance

import kotlin.test.Test
import kotlin.test.assertEquals

class IbanUtilsTest {
    @Test
    fun calculatesSpanishIbanCheckDigits() {
        assertEquals("91", IbanUtils.getSpanishCheckDigits("21000418450200051332"))
        assertEquals("91", IbanUtils.getIban("21000418450200051332"))
    }
}
