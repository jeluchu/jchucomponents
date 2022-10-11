@file:OptIn(ExperimentalMaterialApi::class)

package com.jeluchu.jchucomponents.ui.templates

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.TopAppBar
import androidx.compose.material.contentColorFor
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.core.extensions.strings.empty
import com.jeluchu.jchucomponents.ui.shapes.BottomSheetShape
import com.jeluchu.jchucomponents.ui.snackbar.SnackbarCustomized
import com.jeluchu.jchucomponents.ui.textfields.CountTextFieldPreview
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 *
 * Author: @Jeluchu
 *
 * This component is based on EditText in which you can check
 * how many characters you have typed and what is the maximum
 *
 * @sample CountTextFieldPreview
 *
 * @param modifier optional Modifier for the root of the [Scaffold]
 * @param scaffoldState state of this scaffold widget. It contains the state of the screen, e.g.
 * variables to provide manual control over the drawer behavior, sizes of components, etc
 * @param topBar top app bar of the screen. Consider using [TopAppBar].
 * @param bottomBar bottom bar of the screen. Consider using [BottomAppBar].
 * @param snackbarHost component to host [Snackbar]s that are pushed to be shown via
 * [SnackbarHostState.showSnackbar]. Usually it's a [SnackbarHost]
 * @param floatingActionButton Main action button of your screen. Consider using
 * [FloatingActionButton] for this slot.
 * @param floatingActionButtonPosition position of the FAB on the screen. See [FabPosition] for
 * possible options available.
 * @param isFloatingActionButtonDocked whether [floatingActionButton] should overlap with
 * [bottomBar] for half a height, if [bottomBar] exists. Ignored if there's no [bottomBar] or no
 * [floatingActionButton].
 * @param backgroundColor background of the scaffold body
 * @param contentColor color of the content in scaffold body. Defaults to either the matching
 * content color for [backgroundColor], or, if it is not a color from the theme, this will keep
 * the same value set above this Surface.
 * @param isLoading controls the status of whether the request to the service is loading
 * or has already finished.
 * @param list the list of items to be displayed within the LazyColumn or LazyRow
 * @param success content when service response is success of your screen. The lambda receives an
 * [PaddingValues] that should be applied to the content root via Modifier.padding to
 * properly offset top and bottom bars. If you're using VerticalScroller, apply this
 * modifier to the child of the scroller, and not on the scroller itself.
 * @param failed content when service response is failed of your screen. The lambda receives an
 * [PaddingValues] that should be applied to the content root via Modifier.padding to
 * properly offset top and bottom bars. If you're using VerticalScroller, apply this
 * modifier to the child of the scroller, and not on the scroller itself.
 * @param loading content when service response is loading of your screen. The lambda receives an
 * [PaddingValues] that should be applied to the content root via Modifier.padding to
 * properly offset top and bottom bars. If you're using VerticalScroller, apply this
 * modifier to the child of the scroller, and not on the scroller itself.
 *
 */
@Composable
fun <T> SheetStates(
    modifier: Modifier = Modifier,
    bottomSheetScaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
    scope: CoroutineScope = rememberCoroutineScope(),
    topBar: @Composable () -> Unit = {},
    snackbarHost: @Composable (SnackbarHostState) -> Unit = { SnackbarHost(it) },
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    backgroundColor: Color = MaterialTheme.colors.background,
    contentColor: Color = contentColorFor(backgroundColor),
    isLoading: Boolean = false,
    data: T? = null,
    sheetContent: @Composable ColumnScope.() -> Unit,
    success: @Composable (PaddingValues) -> Unit,
    failed: @Composable (PaddingValues) -> Unit = {},
    loading: @Composable (PaddingValues) -> Unit = {}
) {


    val fabHeightPx = with(LocalDensity.current) { 80.dp.roundToPx().toFloat() }
    val fabOffsetHeightPx = remember { mutableStateOf(40f) }

    val closeSheet: () -> Unit = {
        scope.launch {
            bottomSheetScaffoldState.bottomSheetState.collapse()
        }
    }

    val openSheet: (BottomSheetScreen) -> Unit = {
        scope.launch {
            fabOffsetHeightPx.value = 40f
            bottomSheetScaffoldState.bottomSheetState.expand()
        }
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = fabOffsetHeightPx.value + delta
                if (bottomSheetScaffoldState.bottomSheetState.isCollapsed)
                    fabOffsetHeightPx.value = newOffset.coerceIn(-fabHeightPx, 40f)
                return Offset.Zero
            }
        }
    }

    BottomSheetScaffold(
        modifier = modifier.nestedScroll(nestedScrollConnection),
        sheetPeekHeight = 0.dp,
        scaffoldState = bottomSheetScaffoldState,
        backgroundColor = backgroundColor,
        sheetShape = BottomSheetShape,
        topBar = topBar,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        sheetContent = sheetContent,
        snackbarHost = snackbarHost,
    ) { paddingValues ->
        when {
            !isLoading && data != null -> success(paddingValues)
            !isLoading && data == null -> failed(paddingValues)
            isLoading -> loading(paddingValues)
        }
    }

}