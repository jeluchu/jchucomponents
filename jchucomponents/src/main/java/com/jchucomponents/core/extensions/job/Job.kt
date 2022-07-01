/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jchucomponents.core.extensions.job

import kotlinx.coroutines.Job

fun Job?.cancelIfActive() {
    if (this?.isActive == true) cancel()
}