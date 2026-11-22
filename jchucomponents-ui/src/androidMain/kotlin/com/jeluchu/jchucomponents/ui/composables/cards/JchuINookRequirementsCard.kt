package com.jeluchu.jchucomponents.ui.composables.cards

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeluchu.jchucomponents.ui.R
import com.jeluchu.jchucomponents.ui.composables.chips.JchuAmountCounter
import com.jeluchu.jchucomponents.ui.composables.chips.JchuAmountCounterColors
import com.jeluchu.jchucomponents.ui.extensions.modifier.cornerRadius
import com.jeluchu.jchucomponents.ui.extensions.toImageVector

/** Literal painter-based port of iNook's requirements card. */
@Composable
fun JchuINookRequirementsCard(
    title: String,
    amount: String,
    painter: Painter,
    modifier: Modifier = Modifier,
    colors: JchuINookRequirementsCardColors = JchuINookRequirementsCardColors()
) = Column(
    modifier = modifier
        .fillMaxWidth()
        .border(1.dp, colors.strokeColor, 15.cornerRadius())
        .background(colors.containerColor, 15.cornerRadius())
        .padding(10.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Text(
        text = title,
        maxLines = 1,
        color = colors.contentColor,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.bodySmall
    )
    JchuAmountCounter(
        amount = amount,
        painter = painter,
        colors = colors.amountColors,
        modifier = Modifier.fillMaxWidth()
    )
}

/** Literal vector-based port of iNook's requirements card. */
@Composable
fun JchuINookRequirementsCard(
    title: String,
    amount: String,
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    colors: JchuINookRequirementsCardColors = JchuINookRequirementsCardColors()
) = Column(
    modifier = modifier
        .fillMaxWidth()
        .border(1.dp, colors.strokeColor, 15.cornerRadius())
        .background(colors.containerColor, 15.cornerRadius())
        .padding(10.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Text(
        text = title,
        maxLines = 1,
        color = colors.contentColor,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.labelMedium
    )
    JchuAmountCounter(
        amount = amount,
        imageVector = imageVector,
        colors = colors.amountColors,
        modifier = Modifier.fillMaxWidth()
    )
}

/** Literal text/marquee port of iNook's requirements card. */
@Composable
fun JchuINookRequirementsCard(
    title: String,
    requirement: String,
    modifier: Modifier = Modifier,
    colors: JchuINookRequirementsCardColors = JchuINookRequirementsCardColors()
) = Column(
    modifier = modifier
        .fillMaxWidth()
        .border(1.dp, colors.strokeColor, 15.cornerRadius())
        .background(colors.containerColor, 15.cornerRadius())
        .padding(10.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Text(
        text = title,
        maxLines = 1,
        color = colors.contentColor,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.bodySmall
    )
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.requirementColor.containerColor, 15.cornerRadius())
            .basicMarquee(iterations = Int.MAX_VALUE)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        maxLines = 1,
        text = requirement,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyMedium,
        color = colors.requirementColor.contentColor
    )
}

/** Literal amount-information port of iNook's requirements card. */
@Composable
fun JchuINookRequirementsCard(
    title: String,
    amount: String,
    modifier: Modifier = Modifier,
    colors: JchuINookRequirementsCardColors = JchuINookRequirementsCardColors(),
    dialogDefaults: JchuINookAmountInfoDialogDefaults = JchuINookAmountInfoDialogDefaults()
) = Column(
    modifier = modifier
        .fillMaxWidth()
        .border(1.dp, colors.strokeColor, 15.cornerRadius())
        .background(colors.containerColor, 15.cornerRadius())
        .padding(10.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Text(
        text = title,
        maxLines = 1,
        fontSize = 14.sp,
        style = MaterialTheme.typography.bodyMedium,
        color = colors.contentColor,
        overflow = TextOverflow.Ellipsis
    )
    JchuINookAmountInfo(
        amount = amount,
        dialogDefaults = dialogDefaults,
        colors = colors.amountInfoColors,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun JchuINookAmountInfo(
    amount: String,
    modifier: Modifier = Modifier,
    colors: JchuINookAmountInfoColors = JchuINookAmountInfoColors(),
    dialogDefaults: JchuINookAmountInfoDialogDefaults = JchuINookAmountInfoDialogDefaults()
) = Row(
    modifier = modifier
        .clip(15.cornerRadius())
        .background(colors.containerColor)
        .padding(vertical = 5.dp, horizontal = 8.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
) {
    var isShowDialog by remember { mutableStateOf(false) }

    Text(
        text = amount,
        color = colors.contentColor,
        style = MaterialTheme.typography.bodyLarge
    )
    FilledIconButton(
        shape = 13.cornerRadius(),
        modifier = Modifier.size(35.dp),
        colors = IconButtonDefaults.filledIconButtonColors(
            contentColor = colors.contentColor,
            containerColor = colors.contentColor.copy(alpha = .1f)
        ),
        onClick = { isShowDialog = true }
    ) {
        Icon(
            contentDescription = amount,
            imageVector = R.drawable.jchu_ic_deco_information.toImageVector()
        )
    }

    if (isShowDialog) {
        JchuINookAmountInfoDialog(
            defaults = dialogDefaults,
            onConfirm = { isShowDialog = !isShowDialog }
        )
    }
}

@Composable
private fun JchuINookAmountInfoDialog(
    defaults: JchuINookAmountInfoDialogDefaults,
    onConfirm: () -> Unit
) {
    val darkTheme = isSystemInDarkTheme()
    val dialogContainer = defaults.containerColor.takeOrElse {
        if (darkTheme) Color(0xFF282828) else Color(0xFFFFF6CC)
    }
    val dialogContent = defaults.contentColor.takeOrElse {
        if (darkTheme) Color.White else Color.Black
    }

    AlertDialog(
        onDismissRequest = {},
        icon = {
            Icon(
                modifier = Modifier.size(35.dp),
                imageVector = defaults.icon.toImageVector(),
                contentDescription = null
            )
        },
        title = {
            Text(
                fontSize = 18.sp,
                fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
                text = stringResource(defaults.title),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                fontSize = 13.sp,
                fontFamily = MaterialTheme.typography.bodySmall.fontFamily,
                lineHeight = 20.sp,
                textAlign = TextAlign.Center,
                text = stringResource(defaults.description)
            )
        },
        containerColor = dialogContainer,
        iconContentColor = dialogContent,
        titleContentColor = dialogContent,
        textContentColor = dialogContent.copy(alpha = .6f),
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(R.string.jchu_close),
                    color = dialogContent,
                    fontSize = 14.sp,
                    fontFamily = MaterialTheme.typography.labelLarge.fontFamily
                )
            }
        }
    )
}

@Immutable
class JchuINookRequirementsCardColors(
    val strokeColor: Color = Color.LightGray,
    val contentColor: Color = Color.DarkGray,
    val containerColor: Color = Color.Transparent,
    val amountInfoColors: JchuINookAmountInfoColors = JchuINookAmountInfoColors(),
    val amountColors: JchuAmountCounterColors = JchuAmountCounterColors(),
    val requirementColor: JchuAmountCounterColors = JchuAmountCounterColors()
)

@Immutable
class JchuINookAmountInfoDialogDefaults(
    val contentColor: Color = Color.Unspecified,
    val containerColor: Color = Color.Unspecified,
    @StringRes val title: Int = R.string.jchu_title,
    @StringRes val description: Int = R.string.jchu_description,
    @DrawableRes val icon: Int = R.drawable.jchu_ic_deco_list_add
)

@Immutable
class JchuINookAmountInfoColors(
    val contentColor: Color = Color.Black,
    val containerColor: Color = Color.White
)
