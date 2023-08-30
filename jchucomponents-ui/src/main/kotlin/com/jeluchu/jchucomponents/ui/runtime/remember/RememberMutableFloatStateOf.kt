/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

@file:Suppress("unused")

package com.jeluchu.jchucomponents.ui.runtime.remember

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.Snapshot

/**
 *
 * This is an overload for the original method.
 * If you have any questions, please see the documentation of the original method
 *
 * Remember a new [MutableLongState] initialized with the passed in [value]
 *
 * The MutableState class is a single value holder whose reads and writes are observed by
 * Compose.
 * Additionally, writes to it is transacted as part of the [Snapshot] system.
 *
 * @param value the initial value for the [MutableLongState]
 *
 * @see State
 * @see MutableLongState
 */
@Composable
fun rememberMutableFloatStateOf(
    value: Float
): MutableFloatState = remember { mutableFloatStateOf(value) }

/**
 *
 * This is an overload for the original method.
 * If you have any questions, please see the documentation of the original method
 *
 * Remember a new [MutableLongState] initialized with the passed in [value]
 *
 * The MutableState class is a single value holder whose reads and writes are observed by
 * Compose.
 * Additionally, writes to it is transacted as part of the [Snapshot] system.
 *
 * @param value the initial value for the [MutableLongState]
 *
 * @see State
 * @see MutableLongState
 */
@Composable
fun rememberMutableFloatStateOf(
    key1: Any?,
    value: Float
): MutableFloatState = remember(key1) { mutableFloatStateOf(value) }


/**
 *
 * This is an overload for the original method.
 * If you have any questions, please see the documentation of the original method
 *
 * Remember a new [MutableLongState] initialized with the passed in [value]
 *
 * The MutableState class is a single value holder whose reads and writes are observed by
 * Compose.
 * Additionally, writes to it is transacted as part of the [Snapshot] system.
 *
 * @param value the initial value for the [MutableLongState]
 *
 * @see State
 * @see MutableLongState
 */
@Composable
fun <T> rememberMutableFloatStateOf(
    key1: Any?,
    key2: Any?,
    value: Float
): MutableFloatState = remember(key1, key2) { mutableFloatStateOf(value) }

/**
 *
 * This is an overload for the original method.
 * If you have any questions, please see the documentation of the original method
 *
 * Remember a new [MutableLongState] initialized with the passed in [value]
 *
 * The MutableState class is a single value holder whose reads and writes are observed by
 * Compose.
 * Additionally, writes to it is transacted as part of the [Snapshot] system.
 *
 * @param value the initial value for the [MutableLongState]
 *
 * @see State
 * @see MutableState
 */
@Composable
fun <T> rememberMutableFloatStateOf(
    key1: Any?,
    key2: Any?,
    key3: Any?,
    value: Float
): MutableFloatState = remember(key1, key2, key3) { mutableFloatStateOf(value) }

/**
 *
 * This is an overload for the original method.
 * If you have any questions, please see the documentation of the original method
 *
 * Remember a new [MutableLongState] initialized with the passed in [value]
 *
 * The MutableState class is a single value holder whose reads and writes are observed by
 * Compose.
 * Additionally, writes to it is transacted as part of the [Snapshot] system.
 *
 * @param value the initial value for the [MutableLongState]
 *
 * @see State
 * @see MutableLongState
 */
@Composable
fun rememberMutableFloatStateOf(
    vararg keys: Any?,
    value: Float
): MutableFloatState = remember(keys) { mutableFloatStateOf(value) }