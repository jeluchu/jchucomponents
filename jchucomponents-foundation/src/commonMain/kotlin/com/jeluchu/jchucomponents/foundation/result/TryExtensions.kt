package com.jeluchu.jchucomponents.foundation.result

inline fun <T> tryOrNull(block: () -> T): T? = tryOrDefault(default = null, block = block)

inline fun <T> tryOrDefault(
    default: T?,
    block: () -> T,
): T? =
    try {
        block()
    } catch (_: Throwable) {
        default
    }

inline fun <T> tryOrDefaultNotNull(
    default: T,
    block: () -> T,
): T =
    try {
        block()
    } catch (_: Throwable) {
        default
    }

inline fun <T1 : Any, T2 : Any, T3 : Any, R : Any> safeLet(
    first: T1?,
    second: T2?,
    third: T3?,
    block: (T1, T2, T3) -> R?,
): R? =
    if (first != null && second != null && third != null) {
        block(first, second, third)
    } else {
        null
    }
