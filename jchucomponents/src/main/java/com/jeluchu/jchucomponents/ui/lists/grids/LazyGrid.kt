/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.lists.grids

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> LazyGrid(
    items: List<T> = listOf(),
    rows: Int = 3,
    hPadding: Int = 8,
    itemContent: @Composable LazyItemScope.(T, Int) -> Unit
) {
    val chunkedList = items.chunked(rows)
    LazyColumn(modifier = Modifier.padding(horizontal = hPadding.dp)) {

        itemsIndexed(chunkedList) { index, it ->

            Row {
                it.forEachIndexed { rowIndex, item ->
                    Box(
                        modifier = Modifier
                            .weight(1F)
                            .align(Alignment.Top)
                            .padding(5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        itemContent(item, index * rows + rowIndex)
                    }
                }
                repeat(rows - it.size) {
                    Box(modifier = Modifier.weight(1F)) {}
                }
            }

        }

    }
}