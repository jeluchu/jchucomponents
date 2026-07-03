package com.jeluchu.jchucomponents.foundation.extensions.time

import com.jeluchu.jchucomponents.foundation.time.YearMonth

val YearMonth.nextMonth: YearMonth
    get() = plusMonths(1)

val YearMonth.previousMonth: YearMonth
    get() = minusMonths(1)