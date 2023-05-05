package com.jeluchu.jchucomponents.ui.composables.text

import android.graphics.BlurMaskFilter
import android.text.TextUtils
import android.text.style.LeadingMarginSpan
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.FontRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import kotlin.math.roundToInt

@Composable
fun BlurText(
    text: CharSequence,
    @FontRes font: Int,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    blurRadius: Dp = Dp.Unspecified,
    blurEnabled: Boolean = false,
) = AndroidView(::TextView, modifier) { textView ->
    with(textView) {
        setFontResource(font)
        includeFontPadding = false

        val colorInt = color.takeUnless { it == Color.Unspecified }?.toArgb()
        if (colorInt != null) setTextColor(colorInt)

        val size = fontSize.takeUnless { it == TextUnit.Unspecified }?.value
        if (size != null) setTextSize(TypedValue.COMPLEX_UNIT_SP, size)

        if (blurEnabled) {
            val radius = blurRadius.takeUnless { it == Dp.Unspecified }?.value ?: (textSize / 2f)
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            paint.maskFilter = BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL)
            this.text = buildSpannedString {
                inSpans(LeadingMarginSpan.Standard(radius.roundToInt())) {
                    append(text)
                }
            }
        } else {
            setLayerType(View.LAYER_TYPE_HARDWARE, null)
            paint.maskFilter = null
            this.text = text
        }

        ellipsize = when {
            overflow != TextOverflow.Ellipsis -> null
            else -> TextUtils.TruncateAt.END
        }
        textAlignment = textAlign.toAndroid()
        this.maxLines = maxLines
    }
}

internal fun TextAlign?.toAndroid() =
    when (this) {
        TextAlign.Left -> View.TEXT_ALIGNMENT_TEXT_START
        TextAlign.Right -> View.TEXT_ALIGNMENT_TEXT_END
        TextAlign.Start -> View.TEXT_ALIGNMENT_VIEW_START
        TextAlign.End -> View.TEXT_ALIGNMENT_VIEW_END
        TextAlign.Center -> View.TEXT_ALIGNMENT_CENTER
        else -> View.TEXT_ALIGNMENT_GRAVITY
    }

internal fun TextView.setFontResource(@FontRes id: Int) {
    typeface = ResourcesCompat.getFont(context, id)
}