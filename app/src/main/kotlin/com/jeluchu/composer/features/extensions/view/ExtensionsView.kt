package com.jeluchu.composer.features.extensions.view

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeluchu.composer.core.catalog.CatalogColors
import com.jeluchu.composer.core.catalog.JchuCatalogTheme
import com.jeluchu.composer.core.catalog.ProvideJchuCatalogTheme
import com.jeluchu.composer.core.ui.composables.ScaffoldStructure
import com.jeluchu.composer.core.ui.theme.JeluchuTheme
import com.jeluchu.composer.core.utils.Names
import com.jeluchu.jchucomponents.ui.extensions.modifier.bounceClick
import com.jeluchu.jchucomponents.ui.extensions.modifier.conditional
import com.jeluchu.jchucomponents.ui.extensions.modifier.cornerRadius
import com.jeluchu.jchucomponents.ui.extensions.modifier.Height
import com.jeluchu.jchucomponents.ui.extensions.modifier.Width
import com.jeluchu.jchucomponents.ui.extensions.time.currentTime
import com.jeluchu.jchucomponents.ui.extensions.time.fixedDecimalsTime
import com.jeluchu.jchucomponents.ui.modifiers.dashedBorder

@Composable
fun ExtensionsView(onBack: () -> Unit) {
    ExtensionsCatalog(onBack)
}

@Composable
private fun ExtensionsCatalog(onBack: () -> Unit) {
    var tapCount by remember { mutableIntStateOf(0) }

    ScaffoldStructure(
        title = Names.extensions,
        onNavIconClick = onBack
    ) {
        ExtensionSection("Modifier extensions") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(JchuCatalogTheme.spacing.dimen12)
            ) {
                Box(
                    modifier =
                        Modifier
                            .size(92.dp)
                            .dashedBorder(
                                width = 2.dp,
                                color = JchuCatalogTheme.colors.accent,
                                shape = RoundedCornerShape(12.dp),
                                on = 8.dp,
                                off = 6.dp
                            ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "dashed",
                        color = JchuCatalogTheme.colors.content
                    )
                }
                Box(
                    modifier =
                        Modifier
                            .size(92.dp)
                            .conditional(tapCount % 2 == 0) {
                                Modifier.border(
                                    width = 2.dp,
                                    color = JchuCatalogTheme.colors.accent,
                                    shape = 12.cornerRadius()
                                )
                            }
                            .clip(12.cornerRadius())
                            .background(JchuCatalogTheme.colors.surface)
                            .bounceClick { tapCount++ },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "tap $tapCount",
                        color = JchuCatalogTheme.colors.content
                    )
                }
            }
        }

        ExtensionSection("Spacing extensions") {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier =
                        Modifier
                            .size(48.dp)
                            .clip(8.cornerRadius())
                            .background(JchuCatalogTheme.colors.accent)
                )
                16.Width()
                Text(
                    text = "16.Width()",
                    color = JchuCatalogTheme.colors.content
                )
            }
            12.Height()
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(3.cornerRadius())
                        .background(JchuCatalogTheme.colors.accent.copy(alpha = .45f))
            )
        }

        ExtensionSection("Number and time extensions") {
            val time = currentTime()
            Text(
                text = "fixedDecimalsTime(): ${7.fixedDecimalsTime()}",
                color = JchuCatalogTheme.colors.content
            )
            Text(
                text = "currentTime(): ${time.hours}:${time.minutes} ${time.zoneDay}",
                color = JchuCatalogTheme.colors.content
            )
        }
    }
}

@Composable
private fun ExtensionSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(JchuCatalogTheme.colors.background)
                .padding(JchuCatalogTheme.spacing.dimen12),
        verticalArrangement = Arrangement.spacedBy(JchuCatalogTheme.spacing.dimen08)
    ) {
        Text(
            text = title,
            style = JchuCatalogTheme.typography.section,
            color = JchuCatalogTheme.colors.content
        )
        content()
    }
}

@Preview(name = "Extensions - Light", showBackground = true)
@Composable
private fun ExtensionsLightPreview() {
    JeluchuTheme {
        ExtensionsCatalog(onBack = {})
    }
}

@Preview(
    name = "Extensions - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ExtensionsDarkPreview() {
    JeluchuTheme {
        ProvideJchuCatalogTheme(colors = CatalogColors.dark()) {
            ExtensionsCatalog(onBack = {})
        }
    }
}
