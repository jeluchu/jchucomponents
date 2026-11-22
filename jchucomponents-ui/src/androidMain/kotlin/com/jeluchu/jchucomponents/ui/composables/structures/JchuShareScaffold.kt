package com.jeluchu.jchucomponents.ui.composables.structures

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.ui.R
import com.jeluchu.jchucomponents.ui.composables.toolbars.Toolbar
import com.jeluchu.jchucomponents.ui.composables.toolbars.TopBarSettings

@Composable
fun JchuShareScaffold(
    title: String,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onDownloadClick: () -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null,
    config: JchuShareScaffoldConfig = JchuShareScaffoldConfig(),
    onLoading: @Composable (PaddingValues) -> Unit = { JchuDefaultLoadingContent() },
    onFailure: @Composable (PaddingValues, String?) -> Unit = { _, _ ->
        JchuDefaultEmptyContent("Unable to load content")
    },
    content: @Composable (PaddingValues) -> Unit = {}
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            Toolbar(
                title = title,
                topBarSettings =
                    TopBarSettings(
                        contentColor = config.scaffoldColors.contentColor,
                        backgroundColor = config.scaffoldColors.containerColor
                    )
            ) {
                onBackClick()
            }
        },
        bottomBar = {
            JchuShareBottomBar(
                colors = config.shareBarColors,
                onShareClick = onShareClick,
                onDownloadClick = onDownloadClick
            )
        },
        containerColor = config.scaffoldColors.containerColor,
        contentColor = config.scaffoldColors.contentColor
    ) { paddingValues ->
        JchuRemoteScreenContent(
            isLoading = isLoading,
            error = error,
            onSuccess = { content(paddingValues) },
            onLoading = { onLoading(paddingValues) },
            onFailure = { onFailure(paddingValues, it) }
        )
    }
}

@Composable
fun JchuShareBottomBar(
    onShareClick: () -> Unit,
    onDownloadClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: JchuShareBarColors = JchuShareBarColors(),
    shareText: String = "Share",
    downloadContentDescription: String = "Download",
    downloadIcon: ImageVector = ImageVector.vectorResource(R.drawable.ic_btn_share)
) {
    Row(
        modifier =
            modifier
                .background(
                    color = colors.containerColor,
                    shape =
                        RoundedCornerShape(
                            topEnd = 20.dp,
                            topStart = 20.dp
                        )
                ).animateContentSize()
                .navigationBarsPadding()
    ) {
        Button(
            onClick = onShareClick,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(11.dp)
                    .weight(2f),
            shape = RoundedCornerShape(14.dp),
            colors =
                ButtonDefaults.buttonColors(
                    contentColor = colors.shareContentColor,
                    containerColor = colors.shareContainerColor
                )
        ) {
            Text(text = shareText)
        }

        OutlinedButton(
            onClick = onDownloadClick,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 11.dp,
                        bottom = 11.dp,
                        end = 12.dp
                    ).weight(1f),
            shape = RoundedCornerShape(14.dp),
            border =
                BorderStroke(
                    width = 1.dp,
                    color = colors.downloadContainerColor
                )
        ) {
            Icon(
                tint = colors.downloadContentColor,
                contentDescription = downloadContentDescription,
                imageVector = downloadIcon
            )
        }
    }
}

@Immutable
class JchuShareScaffoldConfig(
    val shareBarColors: JchuShareBarColors = JchuShareBarColors(),
    val scaffoldColors: JchuPurchaseScaffoldColors = JchuPurchaseScaffoldColors()
)

@Immutable
class JchuShareBarColors(
    val containerColor: Color = Color.White,
    val shareContentColor: Color = Color.White,
    val shareContainerColor: Color = Color.Black,
    val downloadContentColor: Color = Color.White,
    val downloadContainerColor: Color = Color.Black
)
