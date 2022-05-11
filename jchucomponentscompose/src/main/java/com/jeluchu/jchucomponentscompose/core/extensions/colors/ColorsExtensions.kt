/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponentscompose.core.extensions.colors

import androidx.compose.ui.graphics.Color

fun Color?.orTransparent(defaultValue: Color = Color.Transparent): Color = this ?: defaultValue
