/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponentscompose.ui.textfields

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeluchu.jchucomponentscompose.core.extensions.strings.empty

@Composable
fun SearchView(
    modifier: Modifier = Modifier,
    state: MutableState<TextFieldValue>,
    labelText: String = String.empty(),
    styleLabel: TextStyle = LocalTextStyle.current,
    colorLabelText: Color = Color.DarkGray,
    cornerRadious: Dp = 15.dp,
    bgContent: Color = Color.DarkGray,
    bgCard: Color = Color.White
) {

    val focusManager = LocalFocusManager.current

    TextField(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadious))
            .background(bgCard)
            .fillMaxWidth()
            .height(53.dp),
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
                    state.value = TextFieldValue(String.empty())
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

}

@Preview(showBackground = true)
@Composable
fun SearchViewPreview() {
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    SearchView(state = textState)
}