package com.jeluchu.jchucomponents.ui.composables.column

import android.os.Build
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jeluchu.jchucomponents.ui.foundation.lists.composition.ListNotOverScroll

/**
 *
 * Author: @Jeluchu
 *
 * This component is customized so that the effect shown at
 * the end of the lists is not shown in versions lower than Android 12.
 * You can use [ListNotOverScroll] to avoid that effect in any list or
 * scroll you have in your application
 *
 * With this component you can include content directly
 * in a column that is vertically scrollable
 *
 * @param modifier modifier that will be used to change the color, size...
 * @param verticalArrangement The vertical arrangement of the layout's children. This allows
 * adding a spacing between items and specifying the arrangement of the items when we have not enough
 * of them to fill the whole minimum size
 * @param horizontalAlignment the horizontal alignment applied to the items
 * @param state the state object to be used to control or observe the column scroll's state
 * @param content items to be displayed within the column
 *
 */
@Composable
fun ScrollableColumn(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    state: ScrollState = rememberScrollState(),
    content: @Composable () -> Unit
) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) { content() }
else ListNotOverScroll {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) { content() }
}