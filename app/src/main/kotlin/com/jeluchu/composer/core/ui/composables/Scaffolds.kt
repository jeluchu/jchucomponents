package com.jeluchu.composer.core.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jeluchu.composer.core.ui.theme.milky
import com.jeluchu.composer.core.ui.theme.secondary
import com.jeluchu.jchucomponents.ui.composables.column.ScrollableColumn
import com.jeluchu.jchucomponents.ui.composables.toolbars.CenterToolbarColors
import com.jeluchu.jchucomponents.ui.composables.toolbars.Toolbar

@Composable
fun ScaffoldStructure(
    title: String,
    navigationIcon: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) = Scaffold(
    topBar = {
        Toolbar(
            title = {
                Text(
                    text = title,
                    fontWeight = FontWeight.ExtraBold
                )
            },
            navigationIcon = navigationIcon,
            colors = CenterToolbarColors(
                containerColor = secondary,
                contentColor = milky
            )
        )
    },
    containerColor = secondary
) { contentPadding ->
    ScrollableColumn(
        modifier = Modifier.padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) { content() }
}