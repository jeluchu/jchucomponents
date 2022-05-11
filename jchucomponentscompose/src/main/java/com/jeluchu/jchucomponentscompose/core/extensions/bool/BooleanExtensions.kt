/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponentscompose.core.extensions.bool

fun Boolean?.orFalse(defaultValue: Boolean = false): Boolean = this ?: defaultValue
