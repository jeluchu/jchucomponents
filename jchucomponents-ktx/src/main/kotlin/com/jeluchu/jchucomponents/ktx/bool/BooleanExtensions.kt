/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ktx.bool

/**
 *
 * [Boolean] Extension to show the original [Boolean] that we pass from
 * the extension or in case of being null we will pass a default value.
 * This default value can be the value you want or the defined one
 * (this would be false)
 *
 * @param defaultValue [Boolean] the value we want in case the Boolean is null,
 * by default it will be false
 *
 */
fun Boolean?.orFalse(defaultValue: Boolean = false): Boolean = this ?: defaultValue

/**
 *
 * [Boolean] Extension that will check if a value is true,
 * performing a check in case it may be nullable
 *
 */
fun Boolean?.isTrue(): Boolean = this != null && this