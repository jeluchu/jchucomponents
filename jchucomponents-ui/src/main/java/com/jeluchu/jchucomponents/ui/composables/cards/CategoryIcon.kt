package com.jeluchu.jchucomponents.ui.composables.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.ui.R
import com.jeluchu.jchucomponents.ui.themes.artichoke

@Composable
fun CategoryIcon(
    modifier: Modifier = Modifier,
    icon: Int,
    backgroundColor: Color,
    contentDescription: String? = null,
    onClick: () -> Unit = {}
) = Surface(
    modifier
        .size(60.dp)
        .clip(RoundedCornerShape(15.dp))
        .background(color = backgroundColor)
        .clickable(onClick = onClick)
) {
    Icon(
        imageVector = ImageVector.vectorResource(id = icon),
        contentDescription = contentDescription,
        tint = artichoke,
        modifier = Modifier.padding(16.dp)
    )
}


@Preview
@Composable
fun CategoryIconPreview() {
    CategoryIcon(
        icon = R.drawable.ic_btn_qrcode,
        backgroundColor = Color(0xFFD4D5C3)
    )
}