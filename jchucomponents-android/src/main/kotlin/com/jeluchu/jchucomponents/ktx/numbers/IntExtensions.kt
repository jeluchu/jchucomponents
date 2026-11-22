/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ktx.numbers

import com.jeluchu.jchucomponents.ktx.constants.FIRST_DAY_OF_MONTH
import java.util.Calendar

/**
 * Return a list of calendar values which contains the months of the year.
 * If the year is the current one, return only the months until the current one.
 */
fun Int.getMonths(): List<Calendar> =
    mutableListOf<Calendar>().also { months ->
        Calendar.getInstance().let { calendar ->
            val maxMonth =
                if (this == calendar.get(Calendar.YEAR)) {
                    calendar.get(Calendar.MONTH)
                } else {
                    Calendar.DECEMBER
                }

            calendar.set(Calendar.YEAR, this)

            for (i in Calendar.JANUARY..maxMonth) {
                months.add(
                    Calendar.getInstance().apply {
                        set(Calendar.YEAR, calendar.get(Calendar.YEAR))
                        set(Calendar.MONTH, i)
                        set(Calendar.DAY_OF_MONTH, FIRST_DAY_OF_MONTH)
                    }
                )
            }
        }
    }

