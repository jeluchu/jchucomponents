/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.foundation.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun noCrash(
    enableLog: Boolean = true,
    func: () -> Unit
): String? =
    runCatching {
        func()
        null
    }.getOrElse {
        if (enableLog) {
            it.printStackTrace()
        }
        it.message
    }

fun doOnUI(
    enableLog: Boolean = true,
    onLog: (text: String) -> Unit = {},
    func: suspend () -> Unit
) = CoroutineScope(Dispatchers.Main).launch {
    noCrashSuspend(enableLog) { func() }?.also { onLog(it) }
}

fun doOnMain(
    enableLog: Boolean = true,
    onLog: (text: String) -> Unit = {},
    func: suspend () -> Unit
) = CoroutineScope(Dispatchers.Default).launch {
    noCrashSuspend(enableLog) { func() }?.also { onLog(it) }
}

suspend fun noCrashSuspend(
    enableLog: Boolean = true,
    func: suspend () -> Unit
): String? =
    runCatchingCancellable {
        func()
        null
    }.getOrElse {
        if (enableLog) {
            it.printStackTrace()
        }
        it.message
    }
