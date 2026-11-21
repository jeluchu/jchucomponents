package com.jeluchu.jchucomponents.config

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JchuAppInfoTest {
    @Test
    fun missingMetadataIsEmptyNotAnError() {
        assertEquals("", JchuAppInfo.metadata("jchu.test.missing.key"))
    }

    @Test
    fun versionsAndPlatformNeverThrow() {
        assertTrue(JchuAppInfo.platform in setOf("android", "ios", "macos", "jvm"))
        assertTrue(JchuAppInfo.versionName.isNotEmpty())
        assertTrue(JchuAppInfo.versionCode.isNotEmpty())
    }
}
