/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.foundation.coroutines

import kotlinx.coroutines.Job

fun Job?.cancelIfActive() {
    if (this?.isActive == true) cancel()
}
