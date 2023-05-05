package com.jeluchu.jchucomponents.extensions.commons

inline fun <T> tryOrNull(block: () -> T) = tryOrDefault(null, block)

inline fun <T> tryOrDefault(default: T?, block: () -> T) =
    try {
        block()
    } catch (_: Throwable) {
        default
    }

inline fun <T> tryOrDefaultNotNull(default: T, block: () -> T) =
    try {
        block()
    } catch (_: Throwable) {
        default
    }

inline fun <T1 : Any, T2 : Any, T3 : Any, R : Any> safeLet(p1: T1?, p2: T2?, p3: T3?, block: (T1, T2, T3) -> R?): R? {
    return if (p1 != null && p2 != null && p3 != null) block(p1, p2, p3) else null
}
