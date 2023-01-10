/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.composables.pager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PageIndicator(
    pagesCount: Int,
    currentPageIndex: Int,
    color: Color,
    modifier: Modifier = Modifier
) = Row(modifier = modifier.wrapContentSize()) {
    for (pageIndex in 0 until pagesCount) {
        val (tint, width) = if (currentPageIndex == pageIndex) color to 16.dp
        else color.copy(alpha = 0.5f) to 4.dp
        Spacer(
            modifier = Modifier
                .padding(4.dp)
                .height(4.dp)
                .width(width)
                .background(tint, RoundedCornerShape(percent = 50))
        )
    }
}
