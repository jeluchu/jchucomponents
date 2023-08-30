/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

@file:Suppress("unused")

package com.jeluchu.jchucomponents.ui.runtime.remember

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.Snapshot

/**
 *
 * This is an overload for the original method.
 * If you have any questions, please see the documentation of the original method
 *
 * Remember a new [MutableIntState] initialized with the passed in [value]
 *
 * The MutableState class is a single value holder whose reads and writes are observed by
 * Compose.
 * Additionally, writes to it is transacted as part of the [Snapshot] system.
 *
 * @param value the initial value for the [MutableIntState]
 *
 * @see State
 * @see MutableIntState
 */
@Composable
fun rememberMutableIntStateOf(
    value: Int,
): MutableIntState = remember { mutableIntStateOf(value) }

/**
 *
 * This is an overload for the original method.
 * If you have any questions, please see the documentation of the original method
 *
 * Remember a new [MutableIntState] initialized with the passed in [value]
 *
 * The MutableState class is a single value holder whose reads and writes are observed by
 * Compose.
 * Additionally, writes to it is transacted as part of the [Snapshot] system.
 *
 * @param value the initial value for the [MutableIntState]
 *
 * @see State
 * @see MutableIntState
 */
@Composable
fun rememberMutableIntStateOf(
    key1: Any?,
    value: Int
): MutableIntState = remember(key1) { mutableIntStateOf(value) }


/**
 *
 * This is an overload for the original method.
 * If you have any questions, please see the documentation of the original method
 *
 * Remember a new [MutableIntState] initialized with the passed in [value]
 *
 * The MutableState class is a single value holder whose reads and writes are observed by
 * Compose.
 * Additionally, writes to it is transacted as part of the [Snapshot] system.
 *
 * @param value the initial value for the [MutableIntState]
 *
 * @see State
 * @see MutableIntState
 */
@Composable
fun <T> rememberMutableIntStateOf(
    key1: Any?,
    key2: Any?,
    value: Int
): MutableIntState = remember(key1, key2) { mutableIntStateOf(value) }

/**
 *
 * This is an overload for the original method.
 * If you have any questions, please see the documentation of the original method
 *
 * Remember a new [MutableIntState] initialized with the passed in [value]
 *
 * The MutableState class is a single value holder whose reads and writes are observed by
 * Compose.
 * Additionally, writes to it is transacted as part of the [Snapshot] system.
 *
 * @param value the initial value for the [MutableIntState]
 *
 * @see State
 * @see MutableState
 */
@Composable
fun <T> rememberMutableIntStateOf(
    key1: Any?,
    key2: Any?,
    key3: Any?,
    value: Int
): MutableIntState = remember(key1, key2, key3) { mutableIntStateOf(value) }

/**
 *
 * This is an overload for the original method.
 * If you have any questions, please see the documentation of the original method
 *
 * Remember a new [MutableIntState] initialized with the passed in [value]
 *
 * The MutableState class is a single value holder whose reads and writes are observed by
 * Compose.
 * Additionally, writes to it is transacted as part of the [Snapshot] system.
 *
 * @param value the initial value for the [MutableIntState]
 *
 * @see State
 * @see MutableIntState
 */
@Composable
fun rememberMutableIntStateOf(
    vararg keys: Any?,
    value: Int
): MutableIntState = remember(keys) { mutableIntStateOf(value) }