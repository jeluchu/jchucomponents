package com.jeluchu.jchucomponentscompose.ui.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.jeluchu.jchucomponentscompose.core.extensions.strings.empty
import com.jeluchu.jchucomponentscompose.ui.images.NetworkImage

/**
 *
 * Author: @Jeluchu
 *
 * Component displaying
 * information on a custom-designed card
 *
 * @param modifier modifier that will be used to change the color, size...
 * @param title text to be displayed on the card
 * @param textColor color of text that appears on Card
 * @param localImage drawable id of the resource you want to be displayed as an icon on Card
 * @param remoteImage link of the image you want to be displayed on Card
 * @param backgroundCard color of the background Card
 * @param navigateToScreen action to be performed after pressing
 *
 */

@Composable
fun SectionCard(
    modifier: Modifier = Modifier,
    title: String,
    textColor: Color,
    localImage: Int = 0,
    remoteImage: String = String.empty(),
    backgroundCard: Color,
    navigateToScreen: () -> Unit
) {

    Card(
        modifier = modifier
            .height(200.dp)
            .clickable { navigateToScreen() },
        backgroundColor = backgroundCard,
        shape = RoundedCornerShape(16.dp),
        elevation = 0.dp
    ) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {

            val (sectionImage, sectionName) = createRefs()

            if (remoteImage.isNotEmpty() && localImage == 0) {
                NetworkImage(
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .constrainAs(sectionImage) {
                            top.linkTo(parent.top, margin = 40.dp)
                            linkTo(parent.start, parent.end)
                        },
                    url = remoteImage
                )
            } else {
                Image(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .constrainAs(sectionImage) {
                            top.linkTo(parent.top, margin = 40.dp)
                            linkTo(parent.start, parent.end)
                        },
                    painter = painterResource(id = localImage),
                    contentDescription = null
                )
            }

            Text(
                text = title,
                modifier = Modifier
                    .padding(12.dp, 6.dp, 12.dp, 6.dp)
                    .constrainAs(sectionName) {
                        bottom.linkTo(parent.bottom, margin = 20.dp)
                        linkTo(parent.start, parent.end)
                    },
                color = textColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.overline,
                textAlign = TextAlign.Center
            )

        }

    }

}