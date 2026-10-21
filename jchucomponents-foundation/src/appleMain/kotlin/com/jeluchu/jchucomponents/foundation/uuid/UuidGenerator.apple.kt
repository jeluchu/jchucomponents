package com.jeluchu.jchucomponents.foundation.uuid

import platform.Foundation.NSUUID

actual fun generateUUID(): String = NSUUID().UUIDString
