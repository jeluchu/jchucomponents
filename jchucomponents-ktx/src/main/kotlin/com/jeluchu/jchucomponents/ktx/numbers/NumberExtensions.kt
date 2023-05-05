package com.jeluchu.jchucomponents.ktx.numbers

fun <T : Number> T.isPositive() = this.toDouble() > 0

fun <T : Number> T.isPositiveOrZero() = this.toDouble() >= 0

fun <T : Number> T.isNegative() = this.toDouble() < 0

fun <T : Number> T.isNegativeOrZero() = this.toDouble() <= 0
