/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.core.states

import com.jeluchu.jchucomponents.core.extensions.strings.empty

data class States<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val isFloatButtom: Boolean = false,
    val error: String = String.empty()
)