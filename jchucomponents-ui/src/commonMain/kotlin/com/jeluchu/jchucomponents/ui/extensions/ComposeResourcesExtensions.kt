package com.jeluchu.jchucomponents.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

/** Resolves this Compose Multiplatform string resource, formatting it with [args]. */
@Composable
fun StringResource.toStringRes(vararg args: Any): String = stringResource(this, *args)

/** Loads this Compose Multiplatform drawable as an [ImageVector] (vector drawables and SVG). */
@Composable
fun DrawableResource.toImageVector(): ImageVector = vectorResource(this)

/** Loads this Compose Multiplatform drawable as a [Painter]. */
@Composable
fun DrawableResource.toPainter(): Painter = painterResource(this)
