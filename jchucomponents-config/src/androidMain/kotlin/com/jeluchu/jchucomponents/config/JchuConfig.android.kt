package com.jeluchu.jchucomponents.config

import android.app.Application
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication

/** Android platform data: the application [context]. */
class JchuAndroidPlatform(
    context: Context
) : JchuPlatform {
    val context: Context = context.applicationContext ?: context
}

/** Configures JchuComponents from your `Application.onCreate`. */
@Suppress("FunctionName")
fun JchuConfig(
    application: Application,
    block: JchuConfigBuilder.() -> Unit
): JchuConfigResult = JchuConfig(JchuAndroidPlatform(application), block)

/** Application context of this platform, or a clear error when `JchuConfig` was started without one. */
val JchuPlatform.androidContext: Context
    get() =
        (this as? JchuAndroidPlatform)?.context
            ?: error("JchuConfig: Android needs a context. Use JchuConfig(application) { ... }.")

internal actual fun KoinApplication.applyJchuPlatform(platform: JchuPlatform) {
    (platform as? JchuAndroidPlatform)?.let { androidContext(it.context) }
}
