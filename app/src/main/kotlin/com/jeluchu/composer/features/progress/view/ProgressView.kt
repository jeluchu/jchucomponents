package com.jeluchu.composer.features.progress.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jeluchu.composer.core.commons.models.MenuOptions
import com.jeluchu.composer.core.ui.composables.SimpleButton
import com.jeluchu.composer.core.ui.theme.darkness
import com.jeluchu.composer.core.ui.theme.milky
import com.jeluchu.composer.core.ui.theme.primary
import com.jeluchu.composer.core.ui.theme.secondary
import com.jeluchu.composer.core.utils.DestinationsIds
import com.jeluchu.composer.core.utils.Names
import com.jeluchu.jchucomponents.ui.accompanist.systemui.SystemStatusBarColors
import com.jeluchu.jchucomponents.ui.composables.column.ScrollableColumn
import com.jeluchu.jchucomponents.ui.extensions.modifier.cornerRadius

@Composable
fun ProgressView(onItemClick: (String) -> Unit) {
    SystemStatusBarColors(
        systemBarsColor = secondary,
        statusBarColor = secondary
    )

    Progress(onItemClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Progress(
    onItemClick: (String) -> Unit
) = Scaffold(
    topBar = {
        TopAppBar(
            title = {
                Text(
                    text = Names.buttons,
                    color = milky,
                    fontWeight = FontWeight.ExtraBold
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = secondary
            )
        )
    },
    containerColor = secondary
) { contentPadding ->
    ScrollableColumn(
        modifier = Modifier.padding(contentPadding)
    ) {
        MenuOptions.progress.forEach { option ->
            SimpleButton(
                modifier = Modifier
                    .padding(
                        horizontal = 15.dp,
                        vertical = 5.dp
                    )
                    .clip(10.cornerRadius())
                    .background(primary.copy(.7f)),
                label = option.name,
                color = Color.DarkGray
            ) { onItemClick(option.id) }
        }
    }
}