/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponentscompose.core.states

import com.jeluchu.jchucomponentscompose.core.extensions.strings.empty

data class ListStates<T>(
    val isLoading: Boolean = false,
    val data: List<T> = emptyList(),
    val isFloatButtom: Boolean = false,
    val error: String = String.empty()
)