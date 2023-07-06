/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.dragndrop.states

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemInfo
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.ui.dragndrop.DragCancelledAnimation
import com.jeluchu.jchucomponents.ui.dragndrop.SpringDragCancelledAnimation
import com.jeluchu.jchucomponents.ui.dragndrop.models.ItemPosition
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun rememberReorderableLazyHorizontalStaggeredGridState(
    onMove: (ItemPosition, ItemPosition) -> Unit,
    gridState: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    canDragOver: ((draggedOver: ItemPosition, dragging: ItemPosition) -> Boolean)? = null,
    onDragEnd: ((startIndex: Int, endIndex: Int) -> (Unit))? = null,
    maxScrollPerFrame: Dp = 20.dp,
    dragCancelledAnimation: DragCancelledAnimation = SpringDragCancelledAnimation(),
) = rememberReorderableLazyStaggeredGridState(
    onMove = onMove,
    gridState = gridState,
    canDragOver = canDragOver,
    onDragEnd = onDragEnd,
    maxScrollPerFrame = maxScrollPerFrame,
    dragCancelledAnimation = dragCancelledAnimation,
    orientation = Orientation.Horizontal
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun rememberReorderableLazyVerticalStaggeredGridState(
    onMove: (ItemPosition, ItemPosition) -> Unit,
    gridState: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    canDragOver: ((draggedOver: ItemPosition, dragging: ItemPosition) -> Boolean)? = null,
    onDragEnd: ((startIndex: Int, endIndex: Int) -> (Unit))? = null,
    maxScrollPerFrame: Dp = 20.dp,
    dragCancelledAnimation: DragCancelledAnimation = SpringDragCancelledAnimation(),
) = rememberReorderableLazyStaggeredGridState(
    onMove = onMove,
    gridState = gridState,
    canDragOver = canDragOver,
    onDragEnd = onDragEnd,
    maxScrollPerFrame = maxScrollPerFrame,
    dragCancelledAnimation = dragCancelledAnimation,
    orientation = Orientation.Vertical
)


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun rememberReorderableLazyStaggeredGridState(
    onMove: (ItemPosition, ItemPosition) -> Unit,
    gridState: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    canDragOver: ((draggedOver: ItemPosition, dragging: ItemPosition) -> Boolean)? = null,
    onDragEnd: ((startIndex: Int, endIndex: Int) -> (Unit))? = null,
    maxScrollPerFrame: Dp = 20.dp,
    dragCancelledAnimation: DragCancelledAnimation = SpringDragCancelledAnimation(),
    orientation: Orientation
): ReorderableLazyStaggeredGridState {
    val maxScroll = with(LocalDensity.current) { maxScrollPerFrame.toPx() }
    val scope = rememberCoroutineScope()
    val state = remember(gridState) {
        ReorderableLazyStaggeredGridState(
            gridState,
            scope,
            maxScroll,
            onMove,
            canDragOver,
            onDragEnd,
            dragCancelledAnimation,
            orientation = orientation
        )
    }
    LaunchedEffect(state) {
        state.visibleItemsChanged()
            .collect { state.onDrag(0, 0) }
    }

    LaunchedEffect(state) {
        while (true) {
            val diff = state.scrollChannel.receive()
            gridState.scrollBy(diff)
        }
    }
    return state
}

@OptIn(ExperimentalFoundationApi::class)
class ReorderableLazyStaggeredGridState(
    val gridState: LazyStaggeredGridState,
    scope: CoroutineScope,
    maxScrollPerFrame: Float,
    onMove: (fromIndex: ItemPosition, toIndex: ItemPosition) -> (Unit),
    canDragOver: ((draggedOver: ItemPosition, dragging: ItemPosition) -> Boolean)? = null,
    onDragEnd: ((startIndex: Int, endIndex: Int) -> (Unit))? = null,
    dragCancelledAnimation: DragCancelledAnimation = SpringDragCancelledAnimation(),
    val orientation: Orientation
) : ReorderableState<LazyStaggeredGridItemInfo>(
    scope = scope,
    maxScrollPerFrame = maxScrollPerFrame,
    onMove = onMove,
    canDragOver = canDragOver,
    onDragEnd = onDragEnd,
    dragCancelledAnimation = dragCancelledAnimation
) {
    override val isVerticalScroll: Boolean
        get() = orientation == Orientation.Vertical // XXX gridState.isVertical is not accessible
    override val LazyStaggeredGridItemInfo.left: Int
        get() = offset.x
    override val LazyStaggeredGridItemInfo.right: Int
        get() = offset.x + size.width
    override val LazyStaggeredGridItemInfo.top: Int
        get() = offset.y
    override val LazyStaggeredGridItemInfo.bottom: Int
        get() = offset.y + size.height
    override val LazyStaggeredGridItemInfo.width: Int
        get() = size.width
    override val LazyStaggeredGridItemInfo.height: Int
        get() = size.height
    override val LazyStaggeredGridItemInfo.itemIndex: Int
        get() = index
    override val LazyStaggeredGridItemInfo.itemKey: Any
        get() = key
    override val visibleItemsInfo: List<LazyStaggeredGridItemInfo>
        get() = gridState.layoutInfo.visibleItemsInfo
    override val viewportStartOffset: Int
        get() = gridState.layoutInfo.viewportStartOffset
    override val viewportEndOffset: Int
        get() = gridState.layoutInfo.viewportEndOffset
    override val firstVisibleItemIndex: Int
        get() = gridState.firstVisibleItemIndex
    override val firstVisibleItemScrollOffset: Int
        get() = gridState.firstVisibleItemScrollOffset

    override suspend fun scrollToItem(index: Int, offset: Int) =
        gridState.scrollToItem(index, offset)
}