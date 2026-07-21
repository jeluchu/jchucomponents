package com.jeluchu.jchucomponents.room

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

actual val JchuRoomCoroutineContext: CoroutineContext = Dispatchers.IO
