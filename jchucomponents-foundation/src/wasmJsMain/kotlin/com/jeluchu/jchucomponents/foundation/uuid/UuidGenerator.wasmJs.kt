package com.jeluchu.jchucomponents.foundation.uuid

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
actual fun generateUUID(): String = Uuid.random().toString()
