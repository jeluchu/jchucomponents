/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

@file:Suppress("unused")

package com.jeluchu.jchucomponents.core.extensions.remember

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SnapshotMutationPolicy
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.runtime.structuralEqualityPolicy

/**
 *
 * This is an overload for the original method.
 * If you have any questions, please see the documentation of the original method
 *
 * Remember a new [MutableState] initialized with the passed in [value]
 *
 * The MutableState class is a single value holder whose reads and writes are observed by
 * Compose.
 * Additionally, writes to it is transacted as part of the [Snapshot] system.
 *
 * @param value the initial value for the [MutableState]
 * @param policy a policy to control how changes are handled in mutable snapshots.
 *
 * @see State
 * @see MutableState
 * @see SnapshotMutationPolicy
 */
@Composable
fun <T> rememberMutableStateOf(
    value: T,
    policy: SnapshotMutationPolicy<T> = structuralEqualityPolicy(),
): MutableState<T> = remember { mutableStateOf(value, policy) }

/**
 *
 * This is an overload for the original method.
 * If you have any questions, please see the documentation of the original method
 *
 * Remember a new [MutableState] initialized with the passed in [value]
 *
 * The MutableState class is a single value holder whose reads and writes are observed by
 * Compose.
 * Additionally, writes to it is transacted as part of the [Snapshot] system.
 *
 * @param value the initial value for the [MutableState]
 * @param policy a policy to control how changes are handled in mutable snapshots.
 *
 * @see State
 * @see MutableState
 * @see SnapshotMutationPolicy
 */
@Composable
fun <T> rememberMutableStateOf(
    key1: Any?,
    value: T,
    policy: SnapshotMutationPolicy<T> = structuralEqualityPolicy(),
): MutableState<T> = remember(key1) { mutableStateOf(value, policy) }


/**
 *
 * This is an overload for the original method.
 * If you have any questions, please see the documentation of the original method
 *
 * Remember a new [MutableState] initialized with the passed in [value]
 *
 * The MutableState class is a single value holder whose reads and writes are observed by
 * Compose.
 * Additionally, writes to it is transacted as part of the [Snapshot] system.
 *
 * @param value the initial value for the [MutableState]
 * @param policy a policy to control how changes are handled in mutable snapshots.
 *
 * @see State
 * @see MutableState
 * @see SnapshotMutationPolicy
 */
@Composable
fun <T> rememberMutableStateOf(
    key1: Any?,
    key2: Any?,
    value: T,
    policy: SnapshotMutationPolicy<T> = structuralEqualityPolicy(),
): MutableState<T> = remember(key1, key2) { mutableStateOf(value, policy) }

/**
 *
 * This is an overload for the original method.
 * If you have any questions, please see the documentation of the original method
 *
 * Remember a new [MutableState] initialized with the passed in [value]
 *
 * The MutableState class is a single value holder whose reads and writes are observed by
 * Compose.
 * Additionally, writes to it is transacted as part of the [Snapshot] system.
 *
 * @param value the initial value for the [MutableState]
 * @param policy a policy to control how changes are handled in mutable snapshots.
 *
 * @see State
 * @see MutableState
 * @see SnapshotMutationPolicy
 */
@Composable
fun <T> rememberMutableStateOf(
    key1: Any?,
    key2: Any?,
    key3: Any?,
    value: T,
    policy: SnapshotMutationPolicy<T> = structuralEqualityPolicy(),
): MutableState<T> = remember(key1, key2, key3) { mutableStateOf(value, policy) }

/**
 *
 * This is an overload for the original method.
 * If you have any questions, please see the documentation of the original method
 *
 * Remember a new [MutableState] initialized with the passed in [value]
 *
 * The MutableState class is a single value holder whose reads and writes are observed by
 * Compose.
 * Additionally, writes to it is transacted as part of the [Snapshot] system.
 *
 * @param value the initial value for the [MutableState]
 * @param policy a policy to control how changes are handled in mutable snapshots.
 *
 * @see State
 * @see MutableState
 * @see SnapshotMutationPolicy
 */
@Composable
fun <T> rememberMutableStateOf(
    vararg keys: Any?,
    value: T,
    policy: SnapshotMutationPolicy<T> = structuralEqualityPolicy(),
): MutableState<T> = remember(keys) { mutableStateOf(value, policy) }