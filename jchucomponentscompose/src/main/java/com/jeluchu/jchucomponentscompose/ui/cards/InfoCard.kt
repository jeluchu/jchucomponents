package com.jeluchu.jchucomponentscompose.ui.cards

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeluchu.jchucomponentscompose.core.extensions.strings.empty
import com.jeluchu.jchucomponentscompose.ui.images.NetworkImage

@Composable
fun InfoCard(
    title: String,
    isRemoteImage: Boolean = true,
    iconResource: Int = 0,
    iconTintColor: Color = Color.White,
    iconImage: String? = String.empty(),
    modifier: Modifier,
    textColor: Color = Color.Gray
) {

    Box(
        modifier = modifier,
    ) {

        Row(
            modifier = Modifier.wrapContentWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {

                if (isRemoteImage) {
                    NetworkImage(
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(34.dp),
                        url = iconImage ?: String.empty()
                    )
                } else {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(id = iconResource),
                        tint = iconTintColor,
                        contentDescription = null
                    )
                }


            }

            Column {
                Text(
                    text = title,
                    modifier = Modifier.padding(12.dp, 6.dp, 12.dp, 6.dp),
                    color = textColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.overline,
                    textAlign = TextAlign.Start
                )
            }

        }


    }
}

@Preview
@Composable
fun InfoCardPreviewLight() {
    InfoCard(
        title = "Info",
        iconImage = "",
        modifier = Modifier,
        textColor = Color.White
    )
}
