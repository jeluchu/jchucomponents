package com.jeluchu.jchucomponents.config

/**
 * Platform data that some features need to initialize themselves.
 *
 * On Android it carries the application `Context` (see `JchuConfig(application) { }`); on iOS,
 * macOS and JVM desktop nothing is needed, so [None] is used.
 */
interface JchuPlatform {
    /** Platform without any required data (iOS, macOS and JVM desktop). */
    object None : JchuPlatform
}
