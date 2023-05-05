package com.jeluchu.jchucomponents.ui.extensions.modifier

import androidx.compose.ui.Modifier
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
inline fun Modifier.conditional(condition: Boolean, factory: () -> Modifier): Modifier {
    contract { callsInPlace(factory, InvocationKind.AT_MOST_ONCE) }
    return if (condition) then(factory()) else this
}

@OptIn(ExperimentalContracts::class)
inline fun <T> Modifier.whenNotNull(any: T?, factory: (T) -> Modifier): Modifier {
    contract { callsInPlace(factory, InvocationKind.AT_MOST_ONCE) }
    return if (any != null) then(factory(any)) else this
}