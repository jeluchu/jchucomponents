package com.jeluchu.jchucomponents.ui.composables.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeluchu.jchucomponents.ui.composables.images.NetworkImage
import com.jeluchu.jchucomponents.ui.extensions.modifier.cornerRadius
import com.jeluchu.jchucomponents.ui.theme.cosmicLatte

/** Literal port of iNook's static first-generation description card. */
@Composable
fun JchuLegacySimpleDescription(
    modifier: Modifier = Modifier,
    image: String,
    title: String,
    description: String,
    shape: Shape = 10.cornerRadius(),
    colors: JchuLegacyExpandableDescriptionColors = JchuLegacyExpandableDescriptionColors()
) = Column(
    modifier = modifier
        .clip(shape)
        .background(colors.containerColor)
        .padding(15.dp),
    verticalArrangement = Arrangement.spacedBy(15.dp)
) {
    Text(
        text = title,
        lineHeight = 20.sp,
        color = colors.contentColor,
        style = MaterialTheme.typography.bodyMedium
    )

    Text(
        text = description,
        style = MaterialTheme.typography.bodySmall,
        lineHeight = 20.sp,
        color = cosmicLatte.copy(alpha = .7f)
    )

    NetworkImage(
        modifier = Modifier
            .fillMaxWidth()
            .clip(10.cornerRadius()),
        url = image
    )
}
