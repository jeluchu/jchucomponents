package com.jeluchu.jchucomponents.ui.composables.column

import android.os.Build

internal actual fun shouldSuppressLegacyOverscroll(): Boolean =
    Build.VERSION.SDK_INT < Build.VERSION_CODES.S
