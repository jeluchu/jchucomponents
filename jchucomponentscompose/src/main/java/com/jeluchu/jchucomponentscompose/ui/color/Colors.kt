/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponentscompose.ui.color

import androidx.compose.ui.graphics.Color

object HexToJetpackColor {
    fun getColor(colorString: String): Color {
        return Color(android.graphics.Color.parseColor("#$colorString"))
    }
}