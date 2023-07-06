package com.jeluchu.jchucomponents.ui.utils.annotations

import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "small font",
    group = "font scales",
    fontScale = 0.5f,
    showBackground = true,
)
@Preview(
    name = "large font",
    group = "font scales",
    fontScale = 1.5f,
    showBackground = true,
)
@Preview(
    name = "normal font",
    group = "font scales",
    showBackground = true,
)
annotation class FontScalingPreview