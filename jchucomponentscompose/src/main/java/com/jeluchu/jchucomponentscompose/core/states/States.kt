/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponentscompose.core.states

import com.jeluchu.jchucomponentscompose.core.extensions.strings.empty

data class States<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val isFloatButtom: Boolean = false,
    val error: String = String.empty()
)