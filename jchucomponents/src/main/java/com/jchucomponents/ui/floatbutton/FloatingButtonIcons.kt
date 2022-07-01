/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jchucomponents.ui.floatbutton

import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.jchucomponents.core.extensions.strings.empty
import com.jchucomponents.ui.theme.cosmicLatte

@Composable
fun FloatIcons(
    modifier: Modifier = Modifier,
    isActive: Boolean,
    activeImageVector: ImageVector,
    disableImageVector: ImageVector,
    iconTint: Color = cosmicLatte
) {
    if (isActive)
        Icon(
            modifier = modifier,
            imageVector = activeImageVector,
            contentDescription = String.empty(),
            tint = iconTint
        )
    else
        Icon(
            modifier = modifier,
            imageVector = disableImageVector,
            contentDescription = String.empty(),
            tint = iconTint
        )
}
