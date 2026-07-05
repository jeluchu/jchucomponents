package com.jeluchu.composer.core.catalog

enum class CatalogScenario(
    val title: String,
    val description: String
) {
    LIGHT(
        title = "Light",
        description = "Default component review with the standard catalog theme."
    ),
    DARK(
        title = "Dark",
        description = "Dark surface review for contrast and color role checks."
    ),
    ACCESSIBILITY(
        title = "Accessibility",
        description = "Large text and long-content review for layout resilience."
    )
}
