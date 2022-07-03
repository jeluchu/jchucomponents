/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.composer

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.core.extensions.context.createQR
import com.jeluchu.jchucomponents.core.extensions.context.locale
import com.jeluchu.jchucomponents.core.extensions.context.openOtherApp
import com.jeluchu.jchucomponents.ui.animations.animateItem
import com.jeluchu.jchucomponents.ui.cards.DebutCard
import com.jeluchu.jchucomponents.ui.cards.StoryCard
import com.jeluchu.jchucomponents.ui.images.DoubleTapAnimation
import com.jeluchu.jchucomponents.ui.images.NetworkImage
import com.jeluchu.jchucomponents.ui.sheets.BottomSheetWithCloseDialog
import com.jeluchu.jchucomponents.ui.textfields.SearchView

class MainActivity : ComponentActivity() {

    private var imageUriState = mutableStateOf<Uri?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PhotoSelector()
        }
    }

    private val selectImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            imageUriState.value = uri
        }

    @Composable
    fun PhotoSelector() {

        val context = LocalContext.current

        val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

        BottomSheetWithCloseDialog() {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    val textState = remember { mutableStateOf(TextFieldValue("")) }
                    SearchView(state = textState)


                    StoryCard(
                        modifier = Modifier.animateItem(),
                        title = locale.country,
                        iconMainUrl = "https://picsum.photos/id/1010/5184/3456",
                        circleImage = R.drawable.ic_btnfavourite,
                        navigateToScreen = {
                            // SUPPORTED API LEVEL S+ CUSTOM TAB
                            //context.openInCustomTab("https://developer.android.com/about/versions/marshmallow/android-6.0?hl=es-419", R.color.teal_700)
                            openOtherApp("com.jeluchu.aruppipro", packageManager)

                        }
                    )

                    DebutCard(
                        title = "NameNameNameNameNameNameNameNameName",
                        image = "https://i.picsum.photos/id/0/5616/3744.jpg?hmac=3GAAioiQziMGEtLbfrdbcoenXoWAW-zlyEAMkfEdBzQ",
                        isDebut = true,
                        nameOfDebut = "Look",
                        iconDebut = "https://img.icons8.com/color/search",
                        bgDebut = Color.Yellow,
                        debubtAlignment = Alignment.BottomEnd,
                        debutShape = RoundedCornerShape(topStart = 20.dp),
                    )

                    Image(bitmap = createQR(R.drawable.qr_logo, "https://www.google.es/")!!.asImageBitmap(), contentDescription = "")

                    DebutCard(
                        image = "https://i.picsum.photos/id/0/5616/3744.jpg?hmac=3GAAioiQziMGEtLbfrdbcoenXoWAW-zlyEAMkfEdBzQ",
                        isDebut = true,
                        iconDebut = "https://img.icons8.com/color/search",
                        bgDebut = Color.Yellow,
                        debubtAlignment = Alignment.BottomEnd,
                        debutShape = RoundedCornerShape(topStart = 20.dp),
                    )

                    /*SectionCard(
                        modifier = Modifier.padding(20.dp),
                        title = "Hello!",
                        remoteImage = "https://picsum.photos/250?image=9",
                        backgroundCard = Color.DarkGray,
                        textColor = Color.White,
                        navigateToScreen = { }
                    )*/

                    /*PostCardTop(
                        title = "Hello!",
                        image = "https://picsum.photos/id/1023/3955/2094",
                        description = "The World is a Vampire!"
                    )*/

                    /*DebutCard(
                        title = "Name",
                        image = "https://i.picsum.photos/id/0/5616/3744.jpg?hmac=3GAAioiQziMGEtLbfrdbcoenXoWAW-zlyEAMkfEdBzQ",
                        isDebut = true,
                        nameOfDebut = "Look",
                        iconDebut = "https://img.icons8.com/color/search",
                        bgDebut = Color.Yellow,
                        navigateToScreen = {  }
                    )*/

                    /*InfoCard(
                        title = "Info",
                        iconImage = "https://img.icons8.com/color/search",
                        modifier = Modifier
                            .clip(RoundedCornerShape(15.dp))
                            .background(Color.Blue.copy(.5f)),
                        textColor = Color.Black
                    )*/


                    /* LazyRow {
                         items(numbers) { item ->
                             StoryCard(
                                 modifier = Modifier.animateItem(),
                                 title = item.toString(),
                                 iconMainUrl = "https://i.picsum.photos/id/1016/3844/2563.jpg?hmac=WEryKFRvTdeae2aUrY-DHscSmZuyYI9jd_-p94stBvc",
                                 circleImage = R.drawable.ic_btnfavourite,
                                 navigateToScreen = { context.shortToast("Clicked!") }
                             )
                         }
                     }*/

                    if (false && imageUriState.value != null) {

                        NetworkImage(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .clickable(
                                    onClick = { selectImageLauncher.launch("image/*") }
                                ),
                            contentScale = ContentScale.Crop,
                            url = imageUriState.value!!
                        )

                        Button(
                            onClick = { selectImageLauncher.launch("image/*") },
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Text("Open Gallery")
                        }

                        DoubleTapAnimation(
                            "https://i.picsum.photos/id/1016/3844/2563.jpg?hmac=WEryKFRvTdeae2aUrY-DHscSmZuyYI9jd_-p94stBvc",
                            iconResource = R.drawable.ic_btnfavourite
                        ) {
                            // Action when double tap
                        }

                    }

                }
            }
        }
    }
}