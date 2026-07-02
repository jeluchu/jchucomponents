package com.jeluchu.composer.core.catalog

import com.jeluchu.jchucomponents.foundation.components.JchuProgressButtonState
import com.jeluchu.jchucomponents.foundation.components.JchuProgressState

enum class CatalogFixtureKind {
    ENABLED,
    DISABLED,
    LOADING,
    ERROR,
    LONG_CONTENT,
}

data class CatalogStateFixture<T>(
    val name: String,
    val kind: CatalogFixtureKind,
    val state: T,
)

object CatalogFixtures {
    const val longContent =
        "JchuComponents catalog fixture with enough content to validate wrapping and scaling."

    val progressButtonStateFixtures = listOf(
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
        ),
    )

    val progressButtonStates = progressButtonStateFixtures.map { it.state }

    val progressStateFixtures = listOf(
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
        ),
    )

    fun progressState(
        title: String,
        value: Double,
        isEnabled: Boolean = true,
        isIndeterminate: Boolean = false,
    ) = JchuProgressState(
        title = title,
        value = value,
        maxValue = 100.0,
        isEnabled = isEnabled,
        isIndeterminate = isIndeterminate,
    )
}
