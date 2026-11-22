package com.jeluchu.jchucomponents.foundation.coroutines

import kotlinx.coroutines.CancellationException

/**
 * Rethrows this exception when it is a [CancellationException], so a `catch (Throwable)` does not swallow
 * the cancellation of the surrounding coroutine.
 */
fun Throwable.rethrowIfCancellation() {
    if (this is CancellationException) throw this
}
