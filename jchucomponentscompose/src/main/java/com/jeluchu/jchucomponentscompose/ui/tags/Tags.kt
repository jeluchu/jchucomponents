package com.jeluchu.jchucomponentscompose.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponentscompose.core.extensions.strings.empty
import com.jeluchu.jchucomponentscompose.ui.images.NetworkImage

@Composable
fun Tag(
    modifier: Modifier,
    name: String,
    textColor: Color,
    isIconShow: Boolean = false,
    iconUrl: String = String.empty()
) {
    ChipTagView(
        modifier = modifier,
        name = name,
        textColor = textColor,
        isIconShow = isIconShow,
        iconUrl = iconUrl
    )
}


@Composable
fun ChipTagView(
    modifier: Modifier,
    name: String,
    textColor: Color,
    isIconShow: Boolean = false,
    modifierTextIcon: Modifier = Modifier.padding(8.dp, 6.dp, 12.dp, 6.dp),
    iconUrl: String = String.empty()
) {
    Box(
        modifier = modifier
    ) {

        Row(
            modifier = Modifier.wrapContentWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (isIconShow && iconUrl.isNotEmpty()) {

                Column {
                    NetworkImage(
                        modifier = Modifier
                            .size(23.dp)
                            .padding(start = 7.dp),
                        contentScale = ContentScale.Fit,
                        url = iconUrl
                    )
                }

            }

            Column {
                Text(
                    text = name,
                    modifier = if (isIconShow) modifierTextIcon else Modifier.padding(
                        12.dp,
                        6.dp,
                        12.dp,
                        6.dp
                    ),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.caption,
                    color = textColor
                )
            }

        }

    }
}

@Preview
@Composable
fun TagPreviewLight() {
    Tag(
        Modifier,
        name = "Male",
        textColor = Color.White
    )
}
