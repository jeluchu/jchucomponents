package com.jeluchu.composer.core.commons.models

import com.jeluchu.composer.core.utils.DestinationsIds
import com.jeluchu.composer.core.utils.Names

data class MenuOptions(
    val id: String,
    val name: String
) {
    companion object {
        val dashboard = listOf(
            MenuOptions(
                id = DestinationsIds.buttons,
                name = Names.buttons
            ),
            MenuOptions(
                id = DestinationsIds.progress,
                name = Names.progress
            ),
        )

        val buttons = listOf(
            MenuOptions(
                id = DestinationsIds.floatingButton,
                name = Names.floatingButtons
            ),
        )

        val progress = listOf(
            MenuOptions(
                id = DestinationsIds.linearProgress,
                name = Names.linearProgress
            ),
            MenuOptions(
                id = DestinationsIds.iconProgress,
                name = Names.iconProgress
            )
        )
    }
}