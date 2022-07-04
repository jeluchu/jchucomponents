/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponentscompose.core.extensions.bool

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
