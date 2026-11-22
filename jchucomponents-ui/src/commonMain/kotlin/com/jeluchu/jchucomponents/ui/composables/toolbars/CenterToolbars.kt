package com.jeluchu.jchucomponents.ui.composables.toolbars

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenterToolbar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    colors: CenterToolbarColors = CenterToolbarColors()
) = CenterAlignedTopAppBar(
    title = title,
    actions = actions,
    modifier = modifier,
    navigationIcon = navigationIcon,
    colors =
        TopAppBarDefaults.topAppBarColors(
            containerColor = colors.containerColor,
            titleContentColor = colors.contentColor,
            actionIconContentColor = colors.contentColor,
            navigationIconContentColor = colors.contentColor
        )
)

@Immutable
data class CenterToolbarColors(
    val containerColor: Color = Color.White,
    val contentColor: Color = Color.DarkGray
)
