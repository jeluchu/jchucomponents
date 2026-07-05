package com.jeluchu.composer.core.catalog

import com.jeluchu.jchucomponents.foundation.components.JchuProgressButtonState
import com.jeluchu.jchucomponents.foundation.components.JchuProgressState
import com.jeluchu.jchucomponents.ui.composables.button.FloatingButtonSize

enum class CatalogFixtureKind {
    ENABLED,
    DISABLED,
    LOADING,
    ERROR,
    LONG_CONTENT
}

data class CatalogStateFixture<T>(
    val name: String,
    val kind: CatalogFixtureKind,
    val state: T
)

data class CatalogFloatingButtonFixture(
    val name: String,
    val kind: CatalogFixtureKind,
    val size: FloatingButtonSize,
    val isEnabled: Boolean = true,
    val contentDescription: String
)

data class CatalogChipFixture(
    val name: String,
    val kind: CatalogFixtureKind,
    val label: String,
    val contentDescription: String
)

data class CatalogLoaderFixture(
    val name: String,
    val kind: CatalogFixtureKind,
    val contentDescription: String
)

data class CatalogInputFixture(
    val name: String,
    val kind: CatalogFixtureKind,
    val label: String,
    val contentDescription: String,
    val maxLength: Int? = null
)

object CatalogFixtures {
    const val longContent =
        "JchuComponents catalog fixture with enough content to validate wrapping and scaling."

    val progressButtonStateFixtures =
        listOf(
            CatalogStateFixture(
                name = "Enabled",
                kind = CatalogFixtureKind.ENABLED,
                state = JchuProgressButtonState(title = "Normal")
            ),
            CatalogStateFixture(
                name = "Loading",
                kind = CatalogFixtureKind.LOADING,
                state = JchuProgressButtonState(title = "Loading", isLoading = true)
            ),
            CatalogStateFixture(
                name = "Disabled",
                kind = CatalogFixtureKind.DISABLED,
                state = JchuProgressButtonState(title = "Disabled", isEnabled = false)
            ),
            CatalogStateFixture(
                name = "Error",
                kind = CatalogFixtureKind.ERROR,
                state = JchuProgressButtonState(title = "Error")
            ),
            CatalogStateFixture(
                name = "Long content",
                kind = CatalogFixtureKind.LONG_CONTENT,
                state = JchuProgressButtonState(title = longContent)
            )
        )

    val progressButtonStates = progressButtonStateFixtures.map { it.state }

    val floatingButtonFixtures =
        listOf(
            CatalogFloatingButtonFixture(
                name = "Large",
                kind = CatalogFixtureKind.ENABLED,
                size = FloatingButtonSize.Large,
                contentDescription = "Large floating button"
            ),
            CatalogFloatingButtonFixture(
                name = "Medium",
                kind = CatalogFixtureKind.ENABLED,
                size = FloatingButtonSize.Medium,
                contentDescription = "Medium floating button"
            ),
            CatalogFloatingButtonFixture(
                name = "Small",
                kind = CatalogFixtureKind.ENABLED,
                size = FloatingButtonSize.Small,
                contentDescription = "Small floating button"
            ),
            CatalogFloatingButtonFixture(
                name = "Disabled",
                kind = CatalogFixtureKind.DISABLED,
                size = FloatingButtonSize.Large,
                isEnabled = false,
                contentDescription = "Disabled floating button"
            )
        )

    val chipFixtures =
        listOf(
            CatalogChipFixture(
                name = "Default",
                kind = CatalogFixtureKind.ENABLED,
                label = "Default",
                contentDescription = "Default chip"
            ),
            CatalogChipFixture(
                name = "Clickable",
                kind = CatalogFixtureKind.ENABLED,
                label = "Clickable",
                contentDescription = "Clickable chip"
            ),
            CatalogChipFixture(
                name = "Selected",
                kind = CatalogFixtureKind.ENABLED,
                label = "Selected",
                contentDescription = "Selected chip"
            ),
            CatalogChipFixture(
                name = "Removable",
                kind = CatalogFixtureKind.ENABLED,
                label = "Remove me",
                contentDescription = "Remove chip"
            ),
            CatalogChipFixture(
                name = "Long content",
                kind = CatalogFixtureKind.LONG_CONTENT,
                label = longContent,
                contentDescription = "Long content chip"
            )
        )

    val tagChipFixtures =
        listOf(
            CatalogChipFixture(
                name = "Tag",
                kind = CatalogFixtureKind.ENABLED,
                label = "Android",
                contentDescription = "Tag chip"
            ),
            CatalogChipFixture(
                name = "Tag with container",
                kind = CatalogFixtureKind.ENABLED,
                label = "Compose",
                contentDescription = "Tag chip with container"
            )
        )

    val youtubeChipFixtures =
        listOf(
            CatalogChipFixture(
                name = "Selected",
                kind = CatalogFixtureKind.ENABLED,
                label = "Selected",
                contentDescription = "Selected YouTube chip"
            ),
            CatalogChipFixture(
                name = "Unselected",
                kind = CatalogFixtureKind.DISABLED,
                label = "Unselected",
                contentDescription = "Unselected YouTube chip"
            )
        )

    val loaderFixtures =
        listOf(
            CatalogLoaderFixture(
                name = "Circular",
                kind = CatalogFixtureKind.LOADING,
                contentDescription = "Circular loading indicator"
            ),
            CatalogLoaderFixture(
                name = "Dots",
                kind = CatalogFixtureKind.LOADING,
                contentDescription = "Dots loading indicator"
            ),
            CatalogLoaderFixture(
                name = "Pulse",
                kind = CatalogFixtureKind.LOADING,
                contentDescription = "Pulse loading indicator"
            )
        )

    val inputFixtures =
        listOf(
            CatalogInputFixture(
                name = "Search",
                kind = CatalogFixtureKind.ENABLED,
                label = "Search components",
                contentDescription = "Search the component catalog"
            ),
            CatalogInputFixture(
                name = "Counted",
                kind = CatalogFixtureKind.ENABLED,
                label = "Component name",
                contentDescription = "Component name with character limit",
                maxLength = 40
            ),
            CatalogInputFixture(
                name = "Expandable search",
                kind = CatalogFixtureKind.ENABLED,
                label = "Search the catalog",
                contentDescription = "Expandable search field"
            ),
            CatalogInputFixture(
                name = "Growing text field",
                kind = CatalogFixtureKind.ENABLED,
                label = "Component notes",
                contentDescription = "Multiline field with character limit",
                maxLength = 120
            ),
            CatalogInputFixture(
                name = "Long content",
                kind = CatalogFixtureKind.LONG_CONTENT,
                label = longContent,
                contentDescription = "Long input label fixture"
            )
        )

    val progressStateFixtures =
        listOf(
            CatalogStateFixture(
                name = "Enabled",
                kind = CatalogFixtureKind.ENABLED,
                state = progressState(title = "Linear", value = 40.0)
            ),
            CatalogStateFixture(
                name = "Disabled",
                kind = CatalogFixtureKind.DISABLED,
                state = progressState(title = "Disabled", value = 40.0, isEnabled = false)
            ),
            CatalogStateFixture(
                name = "Loading",
                kind = CatalogFixtureKind.LOADING,
                state = progressState(title = "Indeterminate", value = 0.0, isIndeterminate = true)
            ),
            CatalogStateFixture(
                name = "Error",
                kind = CatalogFixtureKind.ERROR,
                state = progressState(title = "Error", value = 0.0, isEnabled = false)
            ),
            CatalogStateFixture(
                name = "Long content",
                kind = CatalogFixtureKind.LONG_CONTENT,
                state = progressState(title = longContent, value = 65.0)
            )
        )

    fun progressState(
        title: String,
        value: Double,
        isEnabled: Boolean = true,
        isIndeterminate: Boolean = false
    ) = JchuProgressState(
        title = title,
        value = value,
        maxValue = 100.0,
        isEnabled = isEnabled,
        isIndeterminate = isIndeterminate
    )
}
