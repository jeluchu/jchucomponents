package com.jeluchu.jchucomponents.ui.composables.toolbars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.ui.R
import com.jeluchu.jchucomponents.ui.composables.column.JchuScrollableColumn
import com.jeluchu.jchucomponents.ui.extensions.toImageVector
import com.jeluchu.jchucomponents.ui.foundation.icon.IconLink

@Preview
@Composable
fun CenterToolbarActionsPreview() =
    JchuScrollableColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        CenterToolbar(
            modifier = Modifier,
            title = { Text(text = "Villagers") },
            navigationIcon = {
                IconLink(
                    imageVector = R.drawable.ic_btn_qrcode.toImageVector(),
                    contentDescription = "ToolbarTest",
                    tint = Color.DarkGray,
                    onClick = {}
                )
            }
        )

        CenterToolbar(
            modifier = Modifier,
            title = { Text(text = "Villagers") },
            actions = {
                IconLink(
                    imageVector = R.drawable.ic_btn_qrcode.toImageVector(),
                    contentDescription = "ToolbarTest",
                    tint = Color.DarkGray,
                    onClick = {}
                )
            }
        )

        CenterToolbar(
            modifier = Modifier,
            title = { Text(text = "Villagers") },
            navigationIcon = {
                IconLink(
                    imageVector = R.drawable.ic_arrow_left.toImageVector(),
                    contentDescription = "ToolbarTest",
                    tint = Color.DarkGray,
                    onClick = {}
                )
            },
            actions = {
                IconLink(
                    imageVector = R.drawable.ic_btn_qrcode.toImageVector(),
                    contentDescription = "ToolbarTest",
                    tint = Color.DarkGray,
                    onClick = {}
                )
            }
        )
    }
