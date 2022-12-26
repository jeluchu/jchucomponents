/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ktx.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun noCrash(enableLog: Boolean = true, func: () -> Unit): String? =
    runCatching {
        func()
        null
    }.getOrElse {
        if (enableLog)
            it.printStackTrace()
        it.message
    }

@OptIn(DelicateCoroutinesApi::class)
@Deprecated("GlobalScope is delicated api", ReplaceWith("doOnUI or doOnMain"))
fun doOnGlobal(
    enableLog: Boolean = true,
    onLog: (text: String) -> Unit = {},
    func: suspend () -> Unit,
) {
    GlobalScope.launch {
        noCrashSuspend(enableLog) {
            func()
        }?.also { onLog(it) }
    }
}

fun doOnUI(
    enableLog: Boolean = true,
    onLog: (text: String) -> Unit = {},
    func: suspend () -> Unit,
) = CoroutineScope(Dispatchers.Main).launch {
    noCrashSuspend(enableLog) { func() }?.also { onLog(it) }
}

fun doOnMain(
    enableLog: Boolean = true,
    onLog: (text: String) -> Unit = {},
    func: suspend () -> Unit,
) = CoroutineScope(Dispatchers.IO).launch {
    noCrashSuspend(enableLog) { func() }?.also { onLog(it) }
}

suspend fun noCrashSuspend(enableLog: Boolean = true, func: suspend () -> Unit): String? =
    runCatching {
        func()
        null
    }.getOrElse {
        if (enableLog)
            it.printStackTrace()
        it.message
    }