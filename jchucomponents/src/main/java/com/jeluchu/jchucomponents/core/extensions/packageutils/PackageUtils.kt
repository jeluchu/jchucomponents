/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.core.extensions.packageutils

import android.os.Build

inline val buildIsMarshmallowAndUp: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

inline val buildIsLollipopAndUp: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1

inline val buildIsMAndLower: Boolean
    get() = Build.VERSION.SDK_INT <= Build.VERSION_CODES.M

inline val isLollipop: Boolean
    get() = Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1

inline val buildIsNougatAndUp: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

inline val buildIsPAndLower: Boolean
    get() = Build.VERSION.SDK_INT <= Build.VERSION_CODES.P

inline val buildIsPAndUp: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

inline val buildIsOAndUp: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

inline val buildIsQAndUp: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q