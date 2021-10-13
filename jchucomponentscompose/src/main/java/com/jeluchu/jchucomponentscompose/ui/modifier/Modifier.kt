package com.jeluchu.jchucomponentscompose.ui.modifier

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun Int.cornerRadius() = RoundedCornerShape(this.dp)