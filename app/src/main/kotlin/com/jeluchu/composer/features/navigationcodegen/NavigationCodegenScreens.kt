package com.jeluchu.composer.features.navigationcodegen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.ui.unit.dp
import com.jeluchu.composer.core.catalog.JchuCatalogTheme
import com.jeluchu.composer.core.ui.composables.ScaffoldStructure
import com.jeluchu.composer.features.navigationcodegen.navigation.MobilityRoutes
import com.jeluchu.composer.features.navigationcodegen.navigation.NavigationCodegenRoutes
import com.jeluchu.composer.features.navigationcodegen.navigation.NookCatalogRoutes
import com.jeluchu.jchucomponents.navigation3.BackStackBack
import com.jeluchu.jchucomponents.navigation3.CustomAction
import com.jeluchu.jchucomponents.navigation3.NavigateTo
import com.jeluchu.jchucomponents.navigation3.Screen
import com.jeluchu.jchucomponents.ui.composables.toolbars.CenterToolbarColors
import kotlinx.serialization.Serializable

@Serializable
enum class CityMobilityType {
    Metro,
    Bus,
    BikeShare
}

@Serializable
enum class CatalogKind {
    Stay,
    Food,
    Culture
}

@Screen(graph = "Mobility")
@Composable
internal fun MobilityHomeRoute(
    @BackStackBack onBack: () -> Unit,
    @NavigateTo(route = MobilityRoutes.MobilityLinesRoute::class)
    onOpenLines: (type: CityMobilityType) -> Unit,
    @NavigateTo(route = MobilityRoutes.MobilityMapRoute::class)
    onOpenMap: (type: CityMobilityType) -> Unit,
    @NavigateTo(route = NookCatalogRoutes.NookCatalogHomeRoute::class)
    onOpenNookCatalog: () -> Unit,
    @NavigateTo(route = NavigationCodegenRoutes.AnimeSearchRoute::class)
    onOpenTenraiAnime: () -> Unit
) {
    NavigationDemoScaffold(title = "Movilidad", onBack = onBack) {
        DemoHeader(
            eyebrow = "NAVEGACIÓN GENERADA",
            title = "Moverse por Madrid",
            description = "Rutas tipadas, argumentos serializables y acciones conectadas desde la app."
        )
        DemoSection(
            title = "Transporte",
            description = "Cada tarjeta abre una pantalla del grafo Mobility."
        ) {
            DemoActionCard(
                marker = "M",
                title = "Líneas y horarios",
                subtitle = "Metro, autobuses y BiciMAD",
                onClick = { onOpenLines(CityMobilityType.Metro) }
            )
            DemoActionCard(
                marker = "⌖",
                title = "Mapa de estaciones",
                subtitle = "Abre un punto y revisa sus datos",
                onClick = { onOpenMap(CityMobilityType.Metro) }
            )
        }
        DemoSection(
            title = "Otra feature",
            description = "La navegación tipada puede abrir destinos de otros grafos generados."
        ) {
            DemoActionCard(
                marker = "N",
                title = "Abrir catálogo de lugares",
                subtitle = "Navegación generada: Mobility → NookCatalog",
                onClick = onOpenNookCatalog
            )
            DemoActionCard(
                marker = "A",
                title = "Explorar anime con Tenrai",
                subtitle = "ViewModel · repositorio · API REST",
                onClick = onOpenTenraiAnime
            )
        }
        DemoPills(listOf("@Screen", "enum + String", "ViewModel", "Tenrai API"))
    }
}

@Screen(graph = "Mobility")
@Composable
internal fun MobilityLinesRoute(
    type: CityMobilityType,
    @BackStackBack onBack: () -> Unit,
    @NavigateTo(route = MobilityRoutes.MobilityLineDetailsRoute::class)
    onOpenLine: (
        type: CityMobilityType,
        lineCode: String,
        lineName: String
    ) -> Unit
) {
    val lines = linesFor(type)
    NavigationDemoScaffold(title = "Líneas · ${type.label()}", onBack = onBack) {
        DemoHeader(
            eyebrow = "LISTADO TIPADO",
            title = "Elige una línea",
            description = "El tipo de transporte viaja como enum y la selección aporta código y nombre."
        )
        DemoArgumentList(
            title = "Argumento recibido",
            values = listOf("type" to type.label())
        )
        DemoSection(title = "Disponibles", description = "Toca una línea para abrir su detalle.") {
            lines.forEach { line ->
                DemoActionCard(
                    marker = line.code,
                    title = line.name,
                    subtitle = line.description,
                    onClick = { onOpenLine(type, line.code, line.name) }
                )
            }
        }
    }
}

@Screen(graph = "Mobility")
@Composable
internal fun MobilityLineDetailsRoute(
    type: CityMobilityType,
    lineCode: String,
    lineName: String,
    @BackStackBack onBack: () -> Unit,
    @NavigateTo(route = MobilityRoutes.MobilityScheduleRoute::class)
    onOpenSchedule: (title: String, url: String) -> Unit
) {
    NavigationDemoScaffold(title = "Detalle de línea", onBack = onBack) {
        DemoHeader(
            eyebrow = "DETALLE DE TRANSPORTE",
            title = lineName,
            description = "La pantalla recibe varios argumentos independientes desde la ruta generada."
        )
        DemoArgumentList(
            title = "Argumentos de la ruta",
            values = listOf(
                "type" to type.label(),
                "lineCode" to lineCode,
                "lineName" to lineName
            )
        )
        DemoSection(title = "Información del servicio") {
            DemoDataRow("Frecuencia estimada", "4–6 min")
            DemoDataRow("Estado", "Servicio habitual")
            DemoActionCard(
                marker = "PDF",
                title = "Consultar horarios",
                subtitle = "Abre otra ruta con título y URL",
                onClick = {
                    onOpenSchedule(
                        "Horarios $lineCode · ${type.label()}",
                        "https://www.metromadrid.es/es/viaja-en-metro/lineas"
                    )
                }
            )
        }
    }
}

@Screen(graph = "Mobility")
@Composable
internal fun MobilityMapRoute(
    type: CityMobilityType,
    @BackStackBack onBack: () -> Unit,
    @NavigateTo(route = MobilityRoutes.MobilityPointDetailsRoute::class)
    onOpenPoint: (
        type: CityMobilityType,
        pointId: String,
        pointName: String
    ) -> Unit
) {
    NavigationDemoScaffold(title = "Mapa · ${type.label()}", onBack = onBack) {
        DemoHeader(
            eyebrow = "EXPLORADOR",
            title = "Paradas cercanas",
            description = "El mismo patrón sirve para rutas de mapas con tipo e identificador de punto."
        )
        DemoArgumentList(title = "Filtro activo", values = listOf("type" to type.label()))
        DemoSection(title = "Puntos de ejemplo") {
            pointsFor(type).forEach { point ->
                DemoActionCard(
                    marker = "⌖",
                    title = point.name,
                    subtitle = "ID ${point.id} · ${point.status}",
                    onClick = { onOpenPoint(type, point.id, point.name) }
                )
            }
        }
    }
}

@Screen(graph = "Mobility")
@Composable
internal fun MobilityPointDetailsRoute(
    type: CityMobilityType,
    pointId: String,
    pointName: String,
    @BackStackBack onBack: () -> Unit
) {
    NavigationDemoScaffold(title = "Punto de movilidad", onBack = onBack) {
        DemoHeader(
            eyebrow = "DETALLE DE MAPA",
            title = pointName,
            description = "Tipo, identificador y nombre se conservan al entrar desde cualquier feature."
        )
        DemoArgumentList(
            title = "Argumentos recibidos",
            values = listOf(
                "type" to type.label(),
                "pointId" to pointId,
                "pointName" to pointName
            )
        )
        DemoSection(title = "Datos del punto") {
            DemoDataRow("Coordenadas", "40.4168, -3.7038")
            DemoDataRow("Accesibilidad", "Acceso disponible")
            DemoDataRow("Conexiones", "Metro · autobús")
        }
    }
}

@Screen(graph = "Mobility")
@Composable
internal fun MobilityScheduleRoute(
    title: String,
    url: String,
    @BackStackBack onBack: () -> Unit,
    @CustomAction onOpenUrl: (url: String) -> Unit,
) {
    NavigationDemoScaffold(title = "Horarios", onBack = onBack) {
        DemoHeader(
            eyebrow = "DESTINO CON DATOS",
            title = title,
            description = "La pantalla delega la apertura del enlace a una acción personalizada de la app."
        )
        DemoArgumentList(
            title = "Argumentos recibidos",
            values = listOf("title" to title, "url" to url)
        )
        DemoSection(title = "Vista previa") {
            Text(
                text = "La app decide cómo abrir enlaces externos, por ejemplo con el navegador del sistema.",
                color = JchuCatalogTheme.colors.content
            )
            DemoActionCard(
                marker = "↗",
                title = "Abrir en navegador",
                subtitle = "Callback @CustomAction",
                onClick = { onOpenUrl(url) }
            )
        }
    }
}

@Screen(graph = "NookCatalog")
@Composable
internal fun NookCatalogHomeRoute(
    @BackStackBack onBack: () -> Unit,
    @NavigateTo(route = NookCatalogRoutes.NookCategoryRoute::class)
    onOpenCategory: (
        categoryName: String,
        kind: CatalogKind,
        iconLabel: String,
        nearbyMobility: CityMobilityType
    ) -> Unit
) {
    NavigationDemoScaffold(title = "Lugares", onBack = onBack) {
        DemoHeader(
            eyebrow = "CATÁLOGO · FEATURE INDEPENDIENTE",
            title = "Descubre la ciudad",
            description = "La ruta transporta nombre, enum de categoría, icono y tipo de movilidad cercano."
        )
        DemoSection(title = "Categorías", description = "Cada categoría abre su propio listado.") {
            DemoActionCard(
                marker = "⌂",
                title = "Alojamiento",
                subtitle = "Hoteles y casas con estaciones cercanas",
                onClick = {
                    onOpenCategory("Alojamiento", CatalogKind.Stay, "⌂", CityMobilityType.Metro)
                }
            )
            DemoActionCard(
                marker = "✦",
                title = "Gastronomía",
                subtitle = "Restaurantes y mercados",
                onClick = {
                    onOpenCategory("Gastronomía", CatalogKind.Food, "✦", CityMobilityType.Bus)
                }
            )
            DemoActionCard(
                marker = "▧",
                title = "Cultura",
                subtitle = "Museos y espacios culturales",
                onClick = {
                    onOpenCategory("Cultura", CatalogKind.Culture, "▧", CityMobilityType.BikeShare)
                }
            )
        }
        DemoPills(listOf("graph NookCatalog", "enum + icon", "feature callback"))
    }
}

@Screen(graph = "NookCatalog")
@Composable
internal fun NookCategoryRoute(
    categoryName: String,
    kind: CatalogKind,
    iconLabel: String,
    nearbyMobility: CityMobilityType,
    @BackStackBack onBack: () -> Unit,
    @NavigateTo(route = NookCatalogRoutes.NookPlaceDetailsRoute::class)
    onOpenPlace: (
        categoryName: String,
        kind: CatalogKind,
        nearbyMobility: CityMobilityType,
        placeId: String,
        placeName: String
    ) -> Unit
) {
    NavigationDemoScaffold(title = categoryName, onBack = onBack) {
        DemoHeader(
            eyebrow = "LISTA DE CATEGORÍA",
            title = "$iconLabel $categoryName",
            description = "El detalle conserva la categoría y el tipo de transporte cercano."
        )
        DemoArgumentList(
            title = "Argumentos de categoría",
            values = listOf(
                "categoryName" to categoryName,
                "kind" to kind.label(),
                "iconLabel" to iconLabel,
                "nearbyMobility" to nearbyMobility.label()
            )
        )
        DemoSection(title = "Recomendados") {
            placesFor(kind).forEach { place ->
                DemoActionCard(
                    marker = iconLabel,
                    title = place.name,
                    subtitle = "${place.description} · ${place.id}",
                    onClick = {
                        onOpenPlace(
                            categoryName,
                            kind,
                            nearbyMobility,
                            place.id,
                            place.name
                        )
                    }
                )
            }
        }
    }
}

@Screen(graph = "NookCatalog")
@Composable
internal fun NookPlaceDetailsRoute(
    categoryName: String,
    kind: CatalogKind,
    nearbyMobility: CityMobilityType,
    placeId: String,
    placeName: String,
    @BackStackBack onBack: () -> Unit,
    @NavigateTo(route = MobilityRoutes.MobilityPointDetailsRoute::class)
    onShowOnMap: (
        type: CityMobilityType,
        pointId: String,
        pointName: String
    ) -> Unit
) {
    NavigationDemoScaffold(title = "Detalle de lugar", onBack = onBack) {
        DemoHeader(
            eyebrow = "DETALLE · $categoryName",
            title = placeName,
            description = "La navegación a otra feature también se genera con rutas tipadas."
        )
        DemoArgumentList(
            title = "Argumentos de la ruta",
            values = listOf(
                "categoryName" to categoryName,
                "kind" to kind.label(),
                "placeId" to placeId,
                "placeName" to placeName,
                "nearbyMobility" to nearbyMobility.label()
            )
        )
        DemoSection(title = "Acciones") {
            DemoActionCard(
                marker = "⌖",
                title = "Ver conexión cercana",
                subtitle = "Navegación generada: NookCatalog → Mobility",
                onClick = { onShowOnMap(nearbyMobility, placeId, placeName) }
            )
        }
    }
}

@Composable
private fun NavigationDemoScaffold(
    title: String,
    onBack: () -> Unit,
    content: @Composable () -> Unit
) {
    val colors = JchuCatalogTheme.colors
    ScaffoldStructure(
        title = title,
        onNavIconClick = onBack,
        colors =
            CenterToolbarColors(
                containerColor = colors.background,
                contentColor = colors.content
            ),
        content = content
    )
}

@Composable
private fun DemoHeader(
    eyebrow: String,
    title: String,
    description: String
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 6.dp, bottom = 2.dp),
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Text(
            text = eyebrow.uppercase(),
            color = JchuCatalogTheme.colors.accent,
            style = JchuCatalogTheme.typography.label,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = title,
            color = JchuCatalogTheme.colors.content,
            style = JchuCatalogTheme.typography.title,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = description,
            color = JchuCatalogTheme.colors.content.copy(alpha = .78f),
            style = JchuCatalogTheme.typography.body
        )
    }
}

@Composable
private fun DemoSection(
    title: String,
    description: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(JchuCatalogTheme.colors.surface)
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = title,
            color = JchuCatalogTheme.colors.content,
            style = JchuCatalogTheme.typography.section,
            fontWeight = FontWeight.Bold
        )
        if (description != null) {
            Text(
                text = description,
                color = JchuCatalogTheme.colors.content.copy(alpha = .74f),
                style = JchuCatalogTheme.typography.body
            )
        }
        content()
    }
}

@Composable
private fun DemoActionCard(
    marker: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(JchuCatalogTheme.colors.accent)
                .clickable(role = Role.Button, onClick = onClick)
                .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier =
                Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(JchuCatalogTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = marker,
                color = JchuCatalogTheme.colors.content,
                style = JchuCatalogTheme.typography.label,
                fontWeight = FontWeight.Bold
            )
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(
                text = title,
                color = JchuCatalogTheme.colors.contentInverse,
                style = JchuCatalogTheme.typography.body,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = JchuCatalogTheme.colors.contentInverse.copy(alpha = .82f),
                style = JchuCatalogTheme.typography.label
            )
        }
        Text(
            text = "→",
            color = JchuCatalogTheme.colors.contentInverse,
            style = JchuCatalogTheme.typography.section,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun DemoArgumentList(
    title: String,
    values: List<Pair<String, String>>
) {
    DemoSection(title = title) {
        values.forEach { (name, value) -> DemoDataRow(name, value) }
    }
}

@Composable
private fun DemoDataRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = JchuCatalogTheme.colors.content.copy(alpha = .72f),
            style = JchuCatalogTheme.typography.label
        )
        Text(
            text = value,
            color = JchuCatalogTheme.colors.content,
            style = JchuCatalogTheme.typography.body,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun DemoPills(labels: List<String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        labels.forEach { label ->
            Box(
                modifier =
                    Modifier
                        .clip(RoundedCornerShape(50))
                        .background(JchuCatalogTheme.colors.surface)
                        .padding(horizontal = 10.dp, vertical = 7.dp)
            ) {
                Text(
                    text = label,
                    color = JchuCatalogTheme.colors.content,
                    style = JchuCatalogTheme.typography.label,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

private data class DemoLine(val code: String, val name: String, val description: String)

private data class DemoPoint(val id: String, val name: String, val status: String)

private data class DemoPlace(val id: String, val name: String, val description: String)

private fun linesFor(type: CityMobilityType): List<DemoLine> =
    when (type) {
        CityMobilityType.Metro -> listOf(
            DemoLine("L1", "Pinar de Chamartín · Valdecarros", "20 estaciones · línea azul claro"),
            DemoLine("L3", "Villaverde Alto · Moncloa", "18 estaciones · línea amarilla"),
            DemoLine("L10", "Hospital Infanta Sofía · Puerta del Sur", "31 estaciones · línea azul oscuro")
        )
        CityMobilityType.Bus -> listOf(
            DemoLine("24", "Atocha · El Pozo", "Servicio urbano · EMT Madrid"),
            DemoLine("27", "Embajadores · Plaza de Castilla", "Servicio urbano · EMT Madrid"),
            DemoLine("C1", "Circular centro", "Servicio circular · EMT Madrid")
        )
        CityMobilityType.BikeShare -> listOf(
            DemoLine("B01", "Sol · Gran Vía", "Bicimad · estaciones disponibles"),
            DemoLine("B12", "Retiro · Atocha", "Bicimad · estaciones disponibles"),
            DemoLine("B27", "Moncloa · Universidad", "Bicimad · estaciones disponibles")
        )
    }

private fun pointsFor(type: CityMobilityType): List<DemoPoint> =
    when (type) {
        CityMobilityType.Metro -> listOf(
            DemoPoint("station-sol", "Sol", "Abierta · correspondencia"),
            DemoPoint("station-atocha", "Atocha", "Abierta · Cercanías"),
            DemoPoint("station-moncloa", "Moncloa", "Abierta · intercambiador")
        )
        CityMobilityType.Bus -> listOf(
            DemoPoint("stop-cibeles", "Cibeles", "Servicio regular"),
            DemoPoint("stop-alcala", "Alcalá · Sevilla", "Servicio regular"),
            DemoPoint("stop-granvia", "Gran Vía", "Servicio regular")
        )
        CityMobilityType.BikeShare -> listOf(
            DemoPoint("bike-sol", "Sol · Plaza del Carmen", "12 bicicletas"),
            DemoPoint("bike-retiro", "Retiro · Puerta de Alcalá", "8 bicicletas"),
            DemoPoint("bike-prado", "Paseo del Prado", "5 bicicletas")
        )
    }

private fun placesFor(kind: CatalogKind): List<DemoPlace> =
    when (kind) {
        CatalogKind.Stay -> listOf(
            DemoPlace("stay-101", "Casa del Parque", "Alojamiento · Retiro"),
            DemoPlace("stay-204", "Hotel Central", "Alojamiento · Centro"),
            DemoPlace("stay-318", "Palacio Verde", "Alojamiento · Letras")
        )
        CatalogKind.Food -> listOf(
            DemoPlace("food-052", "Mercado de San Miguel", "Mercado · Austrias"),
            DemoPlace("food-114", "Café del Río", "Cafetería · Madrid Río"),
            DemoPlace("food-208", "Taberna del Prado", "Restaurante · Letras")
        )
        CatalogKind.Culture -> listOf(
            DemoPlace("culture-031", "Museo del Prado", "Museo · Paseo del Prado"),
            DemoPlace("culture-078", "Museo Reina Sofía", "Museo · Atocha"),
            DemoPlace("culture-163", "Matadero Madrid", "Espacio cultural · Legazpi")
        )
    }

private fun CityMobilityType.label(): String =
    when (this) {
        CityMobilityType.Metro -> "Metro"
        CityMobilityType.Bus -> "Autobús"
        CityMobilityType.BikeShare -> "BiciMAD"
    }

private fun CatalogKind.label(): String =
    when (this) {
        CatalogKind.Stay -> "Alojamiento"
        CatalogKind.Food -> "Gastronomía"
        CatalogKind.Culture -> "Cultura"
    }

@Screen(graph = "NavigationCodegen")
@Composable
internal fun SimpleNavigationRoute(
    @BackStackBack onBack: () -> Unit
) {
    NavigationDemoScaffold(title = "Ruta sencilla", onBack = onBack) {
        DemoHeader(
            eyebrow = "SIN ARGUMENTOS",
            title = "Destino tipado",
            description = "La ruta no necesita datos para abrirse. El callback de vuelta se conecta desde el NavDisplay principal."
        )
        DemoSection(title = "Qué genera el procesador") {
            DemoDataRow("Argumentos de ruta", "Ninguno")
            DemoDataRow("Ruta", "SimpleNavigationRoute")
        }
        DemoPills(listOf("@Screen", "NavKey", "backStack compartido"))
    }
}

@Screen(graph = "NavigationCodegen")
@Composable
internal fun ArgumentNavigationRoute(
    itemId: String,
    title: String,
    @BackStackBack onBack: () -> Unit
) {
    NavigationDemoScaffold(title = "Argumentos de ruta", onBack = onBack) {
        DemoHeader(
            eyebrow = "RUTA SERIALIZABLE",
            title = title,
            description = "El host construye una ruta tipada y Navigation 3 conserva estos valores en el back stack."
        )
        DemoArgumentList(
            title = "Argumentos recibidos",
            values = listOf(
                "itemId" to itemId,
                "title" to title
            )
        )
        DemoPills(listOf("String", "argument names", "sin strings de ruta"))
    }
}
