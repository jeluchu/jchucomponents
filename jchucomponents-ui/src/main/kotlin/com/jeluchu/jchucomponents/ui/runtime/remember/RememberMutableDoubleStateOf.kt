/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

@file:Suppress("unused")

package com.jeluchu.jchucomponents.ui.runtime.remember

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.Snapshot

/**
 *
 * This is an overload for the original method.
 * If you have any questions, please see the documentation of the original method
 *
 * Remember a new [MutableDoubleState] initialized with the passed in [value]
 *
 * The MutableState class is a single value holder whose reads and writes are observed by
 * Compose.
 * Additionally, writes to it is transacted as part of the [Snapshot] system.
 *
 * @param value the initial value for the [MutableDoubleState]
 *
 * @see State
 * @see MutableDoubleState
 */
@Composable
fun rememberMutableDoubleStateOf(
    value: Double,
): MutableDoubleState = remember { mutableDoubleStateOf(value) }

/**
 *
 * This is an overload for the original method.
 * If you have any questions, please see the documentation of the original method
 *
 * Remember a new [MutableDoubleState] initialized with the passed in [value]
 *
 * The MutableState class is a single value holder whose reads and writes are observed by
 * Compose.
 * Additionally, writes to it is transacted as part of the [Snapshot] system.
 *
 * @param value the initial value for the [MutableDoubleState]
 *
 * @see State
 * @see MutableDoubleState
 */
@Composable
fun rememberMutableDoubleStateOf(
    key1: Any?,
    value: Double
): MutableDoubleState = remember(key1) { mutableDoubleStateOf(value) }


/**
 *
 * This is an overload for the original method.
 * If you have any questions, please see the documentation of the original method
 *
 * Remember a new [MutableDoubleState] initialized with the passed in [value]
 *
 * The MutableState class is a single value holder whose reads and writes are observed by
 * Compose.
 * Additionally, writes to it is transacted as part of the [Snapshot] system.
 *
 * @param value the initial value for the [MutableDoubleState]
 *
 * @see State
 * @see MutableDoubleState
 */
@Composable
fun <T> rememberMutableDoubleStateOf(
    key1: Any?,
    key2: Any?,
    value: Double
): MutableDoubleState = remember(key1, key2) { mutableDoubleStateOf(value) }

/**
 *
 * This is an overload for the original method.
 * If you have any questions, please see the documentation of the original method
 *
 * Remember a new [MutableDoubleState] initialized with the passed in [value]
 *
 * The MutableState class is a single value holder whose reads and writes are observed by
 * Compose.
 * Additionally, writes to it is transacted as part of the [Snapshot] system.
 *
 * @param value the initial value for the [MutableDoubleState]
 *
 * @see State
 * @see MutableState
 */
@Composable
fun <T> rememberMutableDoubleStateOf(
    key1: Any?,
    key2: Any?,
    key3: Any?,
    value: Double
): MutableDoubleState = remember(key1, key2, key3) { mutableDoubleStateOf(value) }

/**
 *
 * This is an overload for the original method.
 * If you have any questions, please see the documentation of the original method
 *
 * Remember a new [MutableDoubleState] initialized with the passed in [value]
 *
 * The MutableState class is a single value holder whose reads and writes are observed by
 * Compose.
 * Additionally, writes to it is transacted as part of the [Snapshot] system.
 *
 * @param value the initial value for the [MutableDoubleState]
 *
 * @see State
 * @see MutableDoubleState
 */
@Composable
fun rememberMutableDoubleStateOf(
    vararg keys: Any?,
    value: Double
): MutableDoubleState = remember(keys) { mutableDoubleStateOf(value) }