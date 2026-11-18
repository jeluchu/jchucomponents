package com.jeluchu.jchucomponents.foundation.uuid

import java.util.UUID

actual fun generateUUID(): String = UUID.randomUUID().toString()
