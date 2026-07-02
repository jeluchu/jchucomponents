package com.jeluchu.composer.features.dashboard.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.jeluchu.composer.core.catalog.JchuCatalogTheme
import com.jeluchu.composer.core.commons.models.MenuOptions
import com.jeluchu.composer.core.ui.composables.ScaffoldListStructure
import com.jeluchu.composer.core.ui.composables.SimpleButton
import com.jeluchu.composer.core.ui.theme.darkness
import com.jeluchu.composer.core.ui.theme.milky
import com.jeluchu.composer.core.ui.theme.primary
import com.jeluchu.composer.core.ui.theme.secondary
import com.jeluchu.jchucomponents.ui.accompanist.systemui.SystemStatusBarColors
import com.jeluchu.jchucomponents.ui.composables.images.NetworkImage
import com.jeluchu.jchucomponents.ui.composables.toolbars.CenterToolbarColors
import com.jeluchu.jchucomponents.ui.foundation.lists.ListRow

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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Main(
    onItemClick: (String) -> Unit
) = ScaffoldListStructure(
    title = "Jchucomponents",
    navIcon = com.jeluchu.jchucomponents.ui.R.drawable.ic_deco_jeluchu,
    colors = CenterToolbarColors(
        containerColor = primary,
        contentColor = darkness
    )
) {
    stickyHeader {
        Text(
            text = "UI Components",
            color = milky.copy(.8f),
            modifier = Modifier
                .fillMaxWidth()
                .background(secondary)
                .padding(JchuCatalogTheme.spacing.dimen15),
            fontWeight = FontWeight.Bold
        )
    }

    items(MenuOptions.ui) { option ->
        SimpleButton(
            modifier = Modifier
                .padding(horizontal = JchuCatalogTheme.spacing.dimen15)
                .clip(JchuCatalogTheme.shapes.corner10)
                .background(JchuCatalogTheme.colors.surface),
            label = option.name,
            color = JchuCatalogTheme.colors.accent
        ) { onItemClick(option.id) }
    }
}
