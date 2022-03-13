package com.jeluchu.jchucomponentscompose.core.extensions.coroutines

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
) {
    GlobalScope.launch(Dispatchers.Main) {
        noCrashSuspend(enableLog) {
            func()
        }?.also { onLog(it) }
    }
}

fun doOnMain(
    enableLog: Boolean = true,
    onLog: (text: String) -> Unit = {},
    func: suspend () -> Unit,
) {
    GlobalScope.launch(Dispatchers.IO) {
        noCrashSuspend(enableLog) {
            func()
        }?.also { onLog(it) }
    }
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