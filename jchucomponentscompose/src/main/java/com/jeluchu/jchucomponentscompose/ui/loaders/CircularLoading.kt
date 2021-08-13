package com.jeluchu.jchucomponentscompose.ui.loaders

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun CircularLoading(
    isShow: Boolean,
    modifier: Modifier = Modifier.fillMaxSize(),
    colorLoading: Color = Color.Black
) {

    ConstraintLayout(modifier = modifier) {

        val (progress) = createRefs()

        if (isShow) {
            CircularProgressIndicator(
                color = colorLoading,
                modifier = Modifier.constrainAs(progress) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
            )
        }

    }

}