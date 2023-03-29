/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.foundation.lists

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@ExperimentalFoundationApi
@Composable
fun LazyHorizontalGrid(
    cells: GridCells,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: LazyGridScope.() -> Unit
) {
    val scope = LazyGridScopeImpl()
    scope.apply(content)

    when (cells) {
        is GridCells.Fixed ->
            FixedLazyGrid(
                nRows = cells.count,
                modifier = modifier,
                state = state,
                contentPadding = contentPadding,
                scope = scope
            )
        is GridCells.Adaptive ->
            BoxWithConstraints(
                modifier = modifier
            ) {
                val nColumns = maxOf((maxHeight / cells.minSize).toInt(), 1)
                FixedLazyGrid(
                    nRows = nColumns,
                    state = state,
                    contentPadding = contentPadding,
                    scope = scope
                )
            }
    }
}

/**
 * This class describes how cells form columns in vertical grids or rows in horizontal grids.
 */
@ExperimentalFoundationApi
sealed class GridCells {
    /**
     * Combines cells with fixed number rows or columns.
     *
     * For example, for the vertical LazyVerticalGrid Fixed(3) would mean that there are 3 columns 1/3
     * of the parent wide.
     */
    @ExperimentalFoundationApi
    class Fixed(val count: Int) : GridCells()

    /**
     * Combines cells with adaptive number of rows or columns. It will try to position as many rows
     * or columns as possible on the condition that every cell has at least [minSize] space and
     * all extra space distributed evenly.
     *
     * For example, for the vertical LazyVerticalGrid Adaptive(20.dp) would mean that there will be as
     * many columns as possible and every column will be at least 20.dp and all the columns will
     * have equal width. If the screen is 88.dp wide then there will be 4 columns 22.dp each.
     */
    @ExperimentalFoundationApi
    class Adaptive(val minSize: Dp) : GridCells()
}

/**
 * Receiver scope which is used by LazyVerticalGrid.
 */
@ExperimentalFoundationApi
interface LazyGridScope {
    /**
     * Adds a single item to the scope.
     *
     * @param content the content of the item
     */
    fun item(content: @Composable LazyItemScope.() -> Unit)

    /**
     * Adds a [count] of items.
     *
     * @param count the items count
     * @param itemContent the content displayed by a single item
     */
    fun items(count: Int, itemContent: @Composable LazyItemScope.(index: Int) -> Unit)
}

/**
 * Adds a list of items.
 *
 * @param items the data list
 * @param itemContent the content displayed by a single item
 */
@ExperimentalFoundationApi
inline fun <T> LazyGridScope.items(
    items: List<T>,
    crossinline itemContent: @Composable LazyItemScope.(item: T) -> Unit
) = items(items.size) {
    itemContent(items[it])
}

/**
 * Adds a list of items where the content of an item is aware of its index.
 *
 * @param items the data list
 * @param itemContent the content displayed by a single item
 */
@ExperimentalFoundationApi
inline fun <T> LazyGridScope.itemsIndexed(
    items: List<T>,
    crossinline itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit
) = items(items.size) {
    itemContent(it, items[it])
}

/**
 * Adds an array of items.
 *
 * @param items the data array
 * @param itemContent the content displayed by a single item
 */
@ExperimentalFoundationApi
inline fun <T> LazyGridScope.items(
    items: Array<T>,
    crossinline itemContent: @Composable LazyItemScope.(item: T) -> Unit
) = items(items.size) {
    itemContent(items[it])
}

/**
 * Adds an array of items where the content of an item is aware of its index.
 *
 * @param items the data array
 * @param itemContent the content displayed by a single item
 */
@ExperimentalFoundationApi
inline fun <T> LazyGridScope.itemsIndexed(
    items: Array<T>,
    crossinline itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit
) = items(items.size) {
    itemContent(it, items[it])
}

@Composable
@ExperimentalFoundationApi
private fun FixedLazyGrid(
    nRows: Int,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    scope: LazyGridScopeImpl
) {
    val columns = (scope.totalSize + nRows - 1) / nRows
    LazyRow(
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
    ) {
        items(columns) { columnIndex ->
            Column {
                for (rowIndex in 0 until nRows) {
                    val itemIndex = columnIndex * nRows + rowIndex
                    if (itemIndex < scope.totalSize) {
                        Box(
                            modifier = Modifier.wrapContentSize(),
                            propagateMinConstraints = true
                        ) {
                            scope.contentFor(itemIndex, this@items).invoke()
                        }
                    } else {
                        Spacer(Modifier.weight(1f, fill = true))
                    }
                }
            }
        }
    }
}

@ExperimentalFoundationApi
internal class LazyGridScopeImpl : LazyGridScope {
    private val intervals = IntervalList<LazyItemScope.(Int) -> (@Composable () -> Unit)>()

    val totalSize get() = intervals.totalSize

    fun contentFor(index: Int, scope: LazyItemScope): @Composable () -> Unit {
        val interval = intervals.intervalForIndex(index)
        val localIntervalIndex = index - interval.startIndex

        return interval.content(scope, localIntervalIndex)
    }

    override fun item(content: @Composable LazyItemScope.() -> Unit) {
        intervals.add(1) { @Composable { content() } }
    }

    override fun items(count: Int, itemContent: @Composable LazyItemScope.(index: Int) -> Unit) {
        intervals.add(count) {
            @Composable { itemContent(it) }
        }
    }
}

internal class IntervalHolder<T>(
    val startIndex: Int,
    val size: Int,
    val content: T
)

internal class IntervalList<T> {
    private val intervals = mutableListOf<IntervalHolder<T>>()
    internal var totalSize = 0
        private set

    fun add(size: Int, content: T) {
        if (size == 0) {
            return
        }

        val interval = IntervalHolder(
            startIndex = totalSize,
            size = size,
            content = content
        )
        totalSize += size
        intervals.add(interval)
    }

    fun intervalForIndex(index: Int) =
        if (index < 0 || index >= totalSize) {
            throw IndexOutOfBoundsException("Index $index, size $totalSize")
        } else {
            intervals[findIndexOfHighestValueLesserThan(intervals, index)]
        }

    /**
     * Finds the index of the [list] which contains the highest value of [IntervalHolder.startIndex]
     * that is less than or equal to the given [value].
     */
    private fun findIndexOfHighestValueLesserThan(list: List<IntervalHolder<T>>, value: Int): Int {
        var left = 0
        var right = list.lastIndex

        while (left < right) {
            val middle = (left + right) / 2

            val middleValue = list[middle].startIndex
            if (middleValue == value) {
                return middle
            }

            if (middleValue < value) {
                left = middle + 1

                // Verify that the left will not be bigger than our value
                if (value < list[left].startIndex) {
                    return middle
                }
            } else {
                right = middle - 1
            }
        }

        return left
    }
}