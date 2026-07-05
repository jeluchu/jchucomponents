package com.jeluchu.jchucomponents.foundation.functional

sealed class Either<out L, out R> {
    data class Left<out L>(
        val value: L
    ) : Either<L, Nothing>()

    data class Right<out R>(
        val value: R
    ) : Either<Nothing, R>()

    val isLeft: Boolean get() = this is Left<L>
    val isRight: Boolean get() = this is Right<R>

    inline fun <T> fold(
        onLeft: (L) -> T,
        onRight: (R) -> T
    ): T =
        when (this) {
            is Left -> onLeft(value)
            is Right -> onRight(value)
        }
}

fun <L, R> left(value: L): Either<L, R> = Either.Left(value)

fun <L, R> right(value: R): Either<L, R> = Either.Right(value)

inline fun <L, R, T> Either<L, R>.flatMap(transform: (R) -> Either<L, T>): Either<L, T> =
    when (this) {
        is Either.Left -> Either.Left(value)
        is Either.Right -> transform(value)
    }

inline fun <L, R, T> Either<L, R>.map(transform: (R) -> T): Either<L, T> = flatMap { right(transform(it)) }

fun <L, R> Either<L, R>.getOrElse(defaultValue: R): R =
    when (this) {
        is Either.Left -> defaultValue
        is Either.Right -> value
    }
