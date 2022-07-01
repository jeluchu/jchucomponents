/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jchucomponents.core.states

import com.jchucomponents.core.extensions.strings.empty

data class ListStates<T>(
    val isLoading: Boolean = false,
    val data: List<T> = emptyList(),
    val isFloatButtom: Boolean = false,
    val error: String = String.empty()
)