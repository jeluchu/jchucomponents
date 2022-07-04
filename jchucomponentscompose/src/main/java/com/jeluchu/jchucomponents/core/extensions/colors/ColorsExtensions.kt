/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.core.extensions.colors

import androidx.compose.ui.graphics.Color
import kotlin.random.Random.Default.nextFloat

/**
 *
 * [Color] Extension to obtain a random color with the Jetpack Compose format
 *
 * @see androidx.compose.ui.graphics.Color
 *
 */
val Color.Companion.Random
    get(): Color = Color((nextFloat() + 16777215).toInt() or (0xFF shl 24))

/**
 *
 * [Color] Extension to show the original [Color] that we pass from
 * the extension or in case of being null we will pass a default value.
 * This default value can be the value you want or the defined one
 * (this would be [Color.Transparent])
 *
 * @param defaultValue [Color] the value we want in case the Color is null,
 * by default it will be [Color.Transparent])
 *
 * @see androidx.compose.ui.graphics.Color
 *
 */
fun Color?.orTransparent(defaultValue: Color = Color.Transparent): Color = this ?: defaultValue
