package com.jeluchu.jchucomponents.ktx.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.jeluchu.jchucomponents.ktx.R
import com.jeluchu.jchucomponents.ktx.constants.GIGA_BYTES
import com.jeluchu.jchucomponents.ktx.constants.MEGA_BYTES

@Composable
fun Long.toFileSizeText() = this.toFloat().run {
    if (this > GIGA_BYTES)
        stringResource(R.string.filesize_gb).format(this / GIGA_BYTES)
    else stringResource(R.string.filesize_mb).format(this / MEGA_BYTES)
}
