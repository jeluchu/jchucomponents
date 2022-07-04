/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponentscompose.core.extensions.packageutils

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

inline val buildIsMarshmallowAndUp: Boolean
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N)
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

inline val buildIsLollipopAndUp: Boolean
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1

inline val buildIsMAndLower: Boolean
    get() = Build.VERSION.SDK_INT <= Build.VERSION_CODES.M

inline val isLollipop: Boolean
    get() = Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1

inline val buildIsNougatAndUp: Boolean
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N)
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

inline val buildIsPAndLower: Boolean
    get() = Build.VERSION.SDK_INT <= Build.VERSION_CODES.P

inline val buildIsPAndUp: Boolean
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.P)
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

inline val buildIsOAndUp: Boolean
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O)
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

inline val buildIsQAndUp: Boolean
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q