package com.jeluchu.jchucomponents.foundation.coroutines

import kotlinx.coroutines.CancellationException

/**
 * Like [runCatching], but a [CancellationException] is rethrown instead of captured.
 *
 * [runCatching] catches every [Throwable], so inside a coroutine it swallows the cancellation of the surrounding
 * scope and the code after it keeps running as if nothing happened. Use this one whenever [block] can call
 * suspend functions.
 *
 * Every other exception, including [Error]s, ends up in the returned [Result].
 */
inline fun <R> runCatchingCancellable(block: () -> R): Result<R> =
    try {
        Result.success(block())
    } catch (error: CancellationException) {
        throw error
    } catch (error: Throwable) {
        Result.failure(error)
    }

/** Receiver version of [runCatchingCancellable]. */
inline fun <T, R> T.runCatchingCancellable(block: T.() -> R): Result<R> =
    try {
        Result.success(block())
    } catch (error: CancellationException) {
        throw error
    } catch (error: Throwable) {
        Result.failure(error)
    }
