package com.jeluchu.jchucomponents.ui.composables.toolbars

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    colors: CenterToolbarColors = CenterToolbarColors()
) = TopAppBar(
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
