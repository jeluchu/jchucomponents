/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ktx.bool

import com.jeluchu.jchucomponents.foundation.extensions.isTrue as foundationIsTrue
import com.jeluchu.jchucomponents.foundation.extensions.orFalse as foundationOrFalse

/**
 * Returns the value when non-null, or [defaultValue] otherwise.
 */
fun Boolean?.orFalse(defaultValue: Boolean = false): Boolean = foundationOrFalse(defaultValue)

/**
 * Returns `true` only when this nullable value is explicitly `true`.
 */
fun Boolean?.isTrue(): Boolean = foundationIsTrue()
