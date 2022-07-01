/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jchucomponents.ui.textfields

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jchucomponents.core.extensions.strings.empty

/**
 *
 * Author: @Jeluchu
 *
 * This component is based on EditText in which you can check
 * how many characters you have typed and what is the maximum
 *
 * @sample CountTextFieldPreview
 *
 * @param title title to be displayed at the top of the EditText / TextField
 * @param maxLength maximum number of characters the user can type
 * @param backgroundColor the lambda to be invoked when this icon is pressed
 * @param disabledLabelColor color to be displayed when Edit Text / Text Field is disabled
 * @param counterTextColor color of the counter (text) that appears below the Edit Text / Text Field
 *
 */

@Composable
fun CountTextField(
    title: String,
    maxLength: Int,
    backgroundColor: Color,
    disabledLabelColor: Color,
    counterTextColor: Color
) {

    Column {
        var textState by remember { mutableStateOf(String.empty()) }

        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            textAlign = TextAlign.Start,
            color = counterTextColor
        )

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = textState,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = backgroundColor,
                cursorColor = Color.Black,
                disabledLabelColor = disabledLabelColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            onValueChange = {
                if (it.length <= maxLength) textState = it
            },
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            trailingIcon = {
                if (textState.isNotEmpty()) {
                    IconButton(onClick = { textState = "" }) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = null
                        )
                    }
                }
            }
        )
        Text(
            text = "${textState.length} / $maxLength",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            textAlign = TextAlign.End,
            color = counterTextColor
        )
    }

}


@Preview
@Composable
fun CountTextFieldPreview() {

    CountTextField(
        title = "Name",
        maxLength = 110,
        backgroundColor = Color(0xffd8e6ff),
        disabledLabelColor = Color(0xffd8e6ff),
        counterTextColor = Color(0xff76a9ff)
    )

}