package com.jeluchu.compposecomponents

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponentscompose.core.extensions.context.shortToast
import com.jeluchu.jchucomponentscompose.ui.animations.animateItem
import com.jeluchu.jchucomponentscompose.ui.cards.StoryCard
import com.jeluchu.jchucomponentscompose.ui.images.DoubleTapAnimation
import com.jeluchu.jchucomponentscompose.ui.images.NetworkImage

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

        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                LazyRow {
                    items(numbers) { item ->
                        StoryCard(
                            modifier = Modifier.animateItem(),
                            title = item.toString(),
                            iconMainUrl = "https://i.picsum.photos/id/1003/1181/1772.jpg?hmac=oN9fHMXiqe9Zq2RM6XT-RVZkojgPnECWwyEF1RvvTZk",
                            circleImage = R.drawable.ic_btnfavourite,
                            navigateToScreen = { context.shortToast("Clicked!") }
                        )
                    }
                }

                if (imageUriState.value != null) {

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

                }

                Button(
                    onClick = { selectImageLauncher.launch("image/*") },
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    androidx.compose.material.Text("Open Gallery")
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