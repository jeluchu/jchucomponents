/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.migration.textfields

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeluchu.jchucomponents.ktx.strings.empty

@Composable
fun SearchView(
    modifier: Modifier = Modifier,
    state: MutableState<String>,
    labelText: String = String.empty(),
    styleLabel: TextStyle = LocalTextStyle.current,
    focusManager: FocusManager = LocalFocusManager.current,
    colorLabelText: Color = Color.DarkGray,
    cornerRadious: Dp = 15.dp,
    bgContent: Color = Color.DarkGray,
    bgCard: Color = Color.White
) = TextField(
    modifier = modifier
        .clip(RoundedCornerShape(cornerRadious))
        .background(bgCard)
        .fillMaxWidth(),
    label = {
        Text(
            text = labelText,
            style = styleLabel,
            color = colorLabelText
        )
    },
    value = state.value,
    textStyle = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    ),
    onValueChange = { value ->
        state.value = value
    }, colors = TextFieldDefaults.textFieldColors(
        textColor = bgContent,
        disabledTextColor = Color.Transparent,
        backgroundColor = bgCard,
        focusedIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        cursorColor = bgContent
    ),
    leadingIcon = {
        Icon(
            Icons.Default.Search,
            tint = bgContent,
            contentDescription = "",
            modifier = Modifier
                .padding(15.dp)
                .size(24.dp)
        )
    },
    trailingIcon = {
        IconButton(
            onClick = {
                state.value = String.empty()
            },
        ) {
            Icon(
                Icons.Filled.Close,
                tint = bgContent,
                contentDescription = "",
                modifier = Modifier
                    .padding(15.dp)
                    .size(24.dp)
            )
        }
    },
    singleLine = true,
    keyboardActions = KeyboardActions(
        onDone = {
            focusManager.clearFocus()
        }
    ),
    keyboardOptions = KeyboardOptions.Default.copy(
        imeAction = ImeAction.Done
    )
)

@Preview(showBackground = true)
@Composable
fun SearchViewPreview() {
    val textState = remember { mutableStateOf(String.empty()) }
    SearchView(state = textState)
}