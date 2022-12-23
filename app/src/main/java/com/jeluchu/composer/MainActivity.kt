/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.composer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.core.extensions.context.createQR
import com.jeluchu.jchucomponents.core.extensions.context.openInCustomTab
import com.jeluchu.jchucomponents.core.extensions.strings.empty
import com.jeluchu.jchucomponents.ui.runtime.remember.rememberMutableStateOf
import com.jeluchu.jchucomponents.ui.animations.lists.animateItem
import com.jeluchu.jchucomponents.ui.cards.DebutCard
import com.jeluchu.jchucomponents.ui.cards.StoryCard
import com.jeluchu.jchucomponents.ui.sheets.BottomSheetWithCloseDialog
import com.jeluchu.jchucomponents.ui.textfields.SearchView

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PhotoSelector()
        }
    }

    @Composable
    fun PhotoSelector() {

        val fieldValue by rememberMutableStateOf(666)

        val context = LocalContext.current

        BottomSheetWithCloseDialog {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    val textState = remember { mutableStateOf(String.empty()) }
                    SearchView(state = textState)

                    data class Algo(val id: Int, val name: String)

                    StoryCard(
                        modifier = Modifier.animateItem(),
                        title = "Rayo MacQueen",
                        iconMainUrl = "https://picsum.photos/id/1010/5184/3456",
                        circleImage = R.drawable.ic_btnfavourite,
                        navigateToScreen = {
                            // SUPPORTED API LEVEL S+ CUSTOM TAB
                            context.openInCustomTab("https://developer.android.com/about/versions/marshmallow/android-6.0?hl=es-419", R.color.teal_700)
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

                }
            }
        }
    }
}