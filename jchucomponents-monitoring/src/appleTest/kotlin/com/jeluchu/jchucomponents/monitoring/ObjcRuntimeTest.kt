@file:OptIn(kotlinx.cinterop.BetaInteropApi::class, kotlinx.cinterop.ExperimentalForeignApi::class)

package com.jeluchu.jchucomponents.monitoring

import com.jeluchu.jchucomponents.config.JchuPlatform
import com.jeluchu.jchucomponents.monitoring.objc.jchuClassNamed
import com.jeluchu.jchucomponents.monitoring.objc.jchuSendObject0
import platform.Foundation.NSSelectorFromString
import platform.Foundation.NSUserDefaults
import platform.Foundation.valueForKey
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/** Proves the Objective-C runtime mechanism the Firebase backends rely on, using Foundation classes. */
class ObjcRuntimeTest {
    @Test
    fun missingSdkYieldsNoReporters() {
        assertNull(jchuClassNamed("FIRDefinitelyNotLinked"))
        assertNull(createPlatformCrashReporter(JchuPlatform.None))
        assertNull(createPlatformAnalytics(JchuPlatform.None))
    }

    @Test
    fun voidMethodsWithOneAndTwoArgumentsAreSent() {
        val defaults = jchuSendObject0(jchuClassNamed("NSUserDefaults"), NSSelectorFromString("standardUserDefaults"))
        assertNotNull(defaults)
        defaults.sendVoid("setObject:forKey:", "value-from-runtime", "jchu.monitoring.test")
        assertEquals("value-from-runtime", NSUserDefaults.standardUserDefaults.stringForKey("jchu.monitoring.test"))
        defaults.sendVoid("removeObjectForKey:", "jchu.monitoring.test")
        assertNull(NSUserDefaults.standardUserDefaults.stringForKey("jchu.monitoring.test"))
    }

    @Test
    fun booleanArgumentsReachTheReceiver() {
        // -[NSMutableURLRequest setHTTPShouldHandleCookies:] takes a BOOL and is visible through a getter.
        val request = platform.Foundation.NSMutableURLRequest()
        assertEquals(true, cookies(request))
        request.sendBoolean("setHTTPShouldHandleCookies:", false)
        assertEquals(false, cookies(request))
    }

    private fun cookies(request: platform.Foundation.NSMutableURLRequest): Boolean =
        (request.valueForKey("HTTPShouldHandleCookies") as platform.Foundation.NSNumber).boolValue

    @Test
    fun unknownSelectorsAreIgnored() {
        NSUserDefaults.standardUserDefaults.sendVoid("thisSelectorDoesNotExist:", "x")
    }
}
