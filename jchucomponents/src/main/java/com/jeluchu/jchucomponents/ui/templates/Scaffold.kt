package com.jeluchu.jchucomponents.ui.templates

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.BottomAppBar
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.TopAppBar
import androidx.compose.material.contentColorFor
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.jeluchu.jchucomponents.ui.textfields.CountTextFieldPreview

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
fun <T> ScaffoldStates(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable (SnackbarHostState) -> Unit = { SnackbarHost(it) },
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    isFloatingActionButtonDocked: Boolean = false,
    backgroundColor: Color = MaterialTheme.colors.background,
    contentColor: Color = contentColorFor(backgroundColor),
    isLoading: Boolean = false,
    data: List<T> = emptyList(),
    success: @Composable (PaddingValues) -> Unit,
    failed: @Composable (PaddingValues) -> Unit = {},
    loading: @Composable (PaddingValues) -> Unit = {}
) {

    Scaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        isFloatingActionButtonDocked = isFloatingActionButtonDocked,
        backgroundColor = backgroundColor,
        contentColor = contentColor
    ) { paddingValues ->
        when {
            !isLoading && data.isNotEmpty() -> success(paddingValues)
            !isLoading && data.isEmpty() -> failed(paddingValues)
            isLoading -> loading(paddingValues)
        }
    }

}