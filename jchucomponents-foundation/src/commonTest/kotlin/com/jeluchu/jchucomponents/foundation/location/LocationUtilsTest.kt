package com.jeluchu.jchucomponents.foundation.location

import kotlin.math.roundToInt
import kotlin.test.Test
import kotlin.test.assertEquals

class LocationUtilsTest {
    @Test
    fun calculatesDistanceInMeters() {
        val distance =
            getDistanceInMeters(
                latitudeStart = 40.4168,
                longitudeStart = -3.7038,
                latitudeEnd = 41.3874,
                longitudeEnd = 2.1686,
            )

        assertEquals(505, (distance / 1000).roundToInt())
    }
}
