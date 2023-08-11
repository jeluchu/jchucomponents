package com.jeluchu.composer.features.dashboard.view

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jeluchu.composer.core.commons.models.MenuOptions
import com.jeluchu.composer.core.ui.composables.SimpleButton
import com.jeluchu.composer.core.ui.theme.darkness
import com.jeluchu.composer.core.ui.theme.milky
import com.jeluchu.composer.core.ui.theme.primary
import com.jeluchu.composer.core.ui.theme.secondary
import com.jeluchu.composer.core.utils.DestinationsIds
import com.jeluchu.jchucomponents.ui.accompanist.systemui.SystemStatusBarColors
import com.jeluchu.jchucomponents.ui.composables.column.ScrollableColumn
import com.jeluchu.jchucomponents.ui.extensions.modifier.cornerRadius

@Composable
fun MainView(
    onItemClick: (String) -> Unit
) {
    SystemStatusBarColors(
        systemBarsColor = primary,
        statusBarColor = primary
    )

    Main(onItemClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main(
    onItemClick: (String) -> Unit
) = Scaffold(
    topBar = {
        TopAppBar(
            title = {
                Text(
                    text = "Jchucomponents",
                    color = darkness,
                    fontWeight = FontWeight.ExtraBold
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = primary
            )
        )
    },
    containerColor = primary
) { contentPadding ->
    ScrollableColumn(
        modifier = Modifier.padding(contentPadding)
    ) {
        MenuOptions.dashboard.forEach { option ->
            SimpleButton(
                modifier = Modifier
                    .padding(15.dp)
                    .clip(10.cornerRadius())
                    .background(secondary),
                label = option.name,
                color = milky
            ) { onItemClick(option.id) }
        }
    }
}