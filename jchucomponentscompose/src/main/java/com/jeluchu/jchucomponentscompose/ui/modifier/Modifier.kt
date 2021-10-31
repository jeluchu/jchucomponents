package com.jeluchu.jchucomponentscompose.ui.modifier

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Int.cornerRadius() = RoundedCornerShape(this.dp)

@Composable
fun Int.Height() = Spacer(modifier = Modifier.height(this.dp))

@Composable
fun Int.Width() = Spacer(modifier = Modifier.width(this.dp))