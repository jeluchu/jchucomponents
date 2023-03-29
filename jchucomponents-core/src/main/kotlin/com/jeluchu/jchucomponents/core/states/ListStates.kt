/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.core.states

data class ListStates<T>(
    val isLoading: Boolean = false,
    val data: List<T> = emptyList(),
    val isFloatButtom: Boolean = false,
    val error: String = ""
)