package com.jeluchu.jchucomponents.monitoring

import com.jeluchu.jchucomponents.config.JchuPlatform

internal actual fun createPlatformCrashReporter(platform: JchuPlatform): JchuCrashReporter? = null

internal actual fun createPlatformAnalytics(platform: JchuPlatform): JchuAnalytics? = null
