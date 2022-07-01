/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jchucomponents.ui.loaders

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout

/**
 *
 * Author: @Jeluchu
 *
 * This is a component for displaying a circular loading progress
 *
 * @sample CircularLoadingPreview
 *
 * @param isShow the status of whether or not to display
 * @param colorLoading color of the circular progress bar
 *
 */

@Composable
fun CircularLoading(
    isShow: Boolean,
    colorLoading: Color = Color.Black
) {

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {

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

@Preview
@Composable
fun CircularLoadingPreview() {
    CircularLoading(
        isShow = true,
        colorLoading = Color.Black
    )
}