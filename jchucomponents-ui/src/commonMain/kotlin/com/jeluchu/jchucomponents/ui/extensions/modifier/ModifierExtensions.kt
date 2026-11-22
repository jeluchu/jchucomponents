/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.extensions.modifier

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.ui.runtime.remember.rememberMutableFloatStateOf
import com.jeluchu.jchucomponents.ui.runtime.remember.rememberMutableIntStateOf

/** ---- MODIFIER EXT -------------------------------------------------------------------------- **/

fun Int.cornerRadius() = RoundedCornerShape(this.dp)

@Composable
fun Int.Height() = Spacer(modifier = Modifier.height(this.dp))

@Composable
fun Int.Width() = Spacer(modifier = Modifier.width(this.dp))

/** ---- ANIMATION ----------------------------------------------------------------------------- **/

fun Modifier.graphicsCollapse(state: LazyListState) =
    this.composed {
        var scrolledY by rememberMutableFloatStateOf(0f)
        var previousOffset by rememberMutableIntStateOf(0)
        graphicsLayer {
            scrolledY += state.firstVisibleItemScrollOffset - previousOffset
            translationY = scrolledY * 0.5f
            previousOffset = state.firstVisibleItemScrollOffset
        }
    }

/** ---- LISTS  -------------------------------------------------------------------------------- **/

fun Modifier.disableVerticalScroll() =
    this.nestedScroll(
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ) = available.copy(x = 0f)
        }
    )
fun Modifier.disableHorizontalScroll() =
    this.nestedScroll(
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ) = available.copy(y = 0f)
        }
    )
