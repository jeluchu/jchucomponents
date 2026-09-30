package com.jeluchu.composer.features.navigationcodegen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeluchu.composer.core.catalog.CatalogColors
import com.jeluchu.composer.core.catalog.JchuCatalogTheme
import com.jeluchu.composer.core.catalog.ProvideJchuCatalogTheme
import com.jeluchu.composer.core.ui.composables.ScaffoldStructure
import com.jeluchu.composer.core.ui.theme.JeluchuTheme
import com.jeluchu.composer.features.navigationcodegen.navigation.NavigationCodegenRoutes
import com.jeluchu.jchucomponents.navigation3.BackStackBack
import com.jeluchu.jchucomponents.navigation3.NavigateTo
import com.jeluchu.jchucomponents.navigation3.Screen
import com.jeluchu.jchucomponents.ui.composables.toolbars.CenterToolbarColors

@Composable
@Screen(graph = "NavigationCodegen")
fun NavigationCodegenView(
    @BackStackBack onBack: () -> Unit,
    @NavigateTo(route = NavigationCodegenRoutes.SimpleNavigationRoute::class)
    onOpenSimple: () -> Unit,
    @NavigateTo(route = NavigationCodegenRoutes.ArgumentNavigationRoute::class)
    onOpenArguments: (itemId: String, title: String) -> Unit,
    @NavigateTo(route = NavigationCodegenRoutes.AnimeSearchRoute::class)
    onOpenViewModel: () -> Unit
) {
    val colors = JchuCatalogTheme.colors

    ScaffoldStructure(
        title = "Navigation 3",
        onNavIconClick = onBack,
        colors =
            CenterToolbarColors(
                containerColor = colors.background,
                contentColor = colors.content
            )
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "Prueba los distintos tipos de navegación generada.",
                color = colors.content,
                style = JchuCatalogTheme.typography.body
            )
            NavigationExampleCard(
                number = "01",
                title = "Navegación simple",
                description = "Abre una pantalla sin argumentos de ruta.",
                onClick = onOpenSimple
            )
            NavigationExampleCard(
                number = "02",
                title = "Navegación con argumentos",
                description = "Envía el identificador y el título como argumentos tipados.",
                onClick = {
                    onOpenArguments("JCHU-03", "Rutas con argumentos")
                }
            )
            NavigationExampleCard(
                number = "03",
                title = "Navegación con ViewModel",
                description = "Koin inyecta el ViewModel, que busca anime a través de Tenrai.",
                onClick = onOpenViewModel
            )
        }
    }
}

@Composable
private fun NavigationExampleCard(
    number: String,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    val colors = JchuCatalogTheme.colors

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(colors.surface)
                .clickable(role = Role.Button, onClick = onClick)
                .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier =
                Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(colors.accent),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number,
                color = colors.contentInverse,
                style = JchuCatalogTheme.typography.label,
                fontWeight = FontWeight.Bold
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                color = colors.content,
                style = JchuCatalogTheme.typography.section,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                color = colors.content.copy(alpha = .78f),
                style = JchuCatalogTheme.typography.body
            )
        }
        Text(
            text = "›",
            color = colors.accent,
            style = JchuCatalogTheme.typography.title,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Preview(name = "Navigation codegen - Light", showBackground = true)
@Composable
private fun NavigationCodegenLightPreview() {
    JeluchuTheme {
        NavigationCodegenView(
            onBack = {},
            onOpenSimple = {},
            onOpenArguments = { _, _ -> },
            onOpenViewModel = {}
        )
    }
}

@Preview(
    name = "Navigation codegen - Dark",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun NavigationCodegenDarkPreview() {
    JeluchuTheme {
        ProvideJchuCatalogTheme(colors = CatalogColors.dark()) {
            NavigationCodegenView(
                onBack = {},
                onOpenSimple = {},
                onOpenArguments = { _, _ -> },
                onOpenViewModel = {}
            )
        }
    }
}
