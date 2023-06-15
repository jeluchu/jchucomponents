/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ui.composables.textfields

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    state: MutableState<String>,
    labelText: String = String.empty(),
    styleLabel: TextStyle = LocalTextStyle.current,
    focusManager: FocusManager = LocalFocusManager.current,
    searchField: SearchField = SearchField(),
) = TextField(
    modifier = modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(searchField.cornerRadious))
        .background(searchField.backgroundColor),
    label = {
        Text(
            text = labelText,
            style = styleLabel,
            color = searchField.labelColor
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
        focusedTextColor = searchField.contentColor,
        disabledTextColor = Color.Transparent,
        containerColor = searchField.backgroundColor,
        focusedIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        cursorColor = searchField.contentColor
    ),
    leadingIcon = {
        Icon(
            Icons.Default.Search,
            tint = searchField.contentColor,
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
                imageVector = Icons.Filled.Close,
                tint = searchField.contentColor,
                contentDescription = String.empty(),
                modifier = Modifier
                    .padding(5.dp)
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

@Immutable
class SearchField constructor(
    val labelColor: Color = Color.DarkGray,
    val cornerRadious: Dp = 15.dp,
    val contentColor: Color = Color.DarkGray,
    val backgroundColor: Color = Color.White
)

@Preview
@Composable
fun SearchViewPreview() {
    val textState = remember { mutableStateOf(String.empty()) }
    SearchTextField(state = textState)
}