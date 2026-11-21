package com.jeluchu.jchucomponents.config

import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class JchuConfigTest {
    @AfterTest
    fun tearDown() = stopKoin()

    private class Recorder(
        override val id: String,
        override val dependsOn: Set<String> = emptySet(),
        private val log: MutableList<String>,
        private val contributed: List<Module> = emptyList()
    ) : JchuFeature {
        override fun initialize(platform: JchuPlatform) {
            log += id
        }

        override fun modules(platform: JchuPlatform) = contributed
    }

    @Test
    fun initializesOnlyInstalledFeaturesInDependencyOrder() {
        val log = mutableListOf<String>()

        JchuConfig {
            install(Recorder("supabase", setOf("prefs"), log))
            install(Recorder("prefs", log = log))
        }

        assertEquals(listOf("prefs", "supabase"), log)
    }

    @Test
    fun missingDependencyFailsWithAClearMessage() {
        val error =
            assertFailsWith<IllegalStateException> {
                JchuConfig { install(Recorder("supabase", setOf("prefs"), mutableListOf())) }
            }
        assertTrue("'prefs'" in error.message.orEmpty())
    }

    @Test
    fun duplicateFeatureIsRejected() {
        assertFailsWith<IllegalArgumentException> {
            JchuConfig {
                install(Recorder("prefs", log = mutableListOf()))
                install(Recorder("prefs", log = mutableListOf()))
            }
        }
    }

    @Test
    fun dependencyCycleIsRejected() {
        assertFailsWith<IllegalStateException> {
            JchuConfig {
                install(Recorder("a", setOf("b"), mutableListOf()))
                install(Recorder("b", setOf("a"), mutableListOf()))
            }
        }
    }

    @Test
    fun doesNotStartKoinWithoutKoinBlock() {
        val result = JchuConfig { install(Recorder("prefs", log = mutableListOf(), contributed = listOf(module { }))) }

        assertNull(result.koinApplication)
        assertEquals(1, result.modules.size)
    }

    @Test
    fun koinBlockStartsKoinWithFeatureAndAppModules() {
        val result =
            JchuConfig {
                install(Recorder("prefs", log = mutableListOf(), contributed = listOf(module { single { "from-feature" } })))
                koin { modules(module { single { 42 } }) }
            }

        val koin = requireNotNull(result.koinApplication).koin
        assertEquals("from-feature", koin.get<String>())
        assertEquals(42, koin.get<Int>())
    }

    @Test
    fun initializationFailureNamesTheFeature() {
        val failing =
            object : JchuFeature {
                override val id = "boom"

                override fun initialize(platform: JchuPlatform) = error("no key")
            }

        val error = assertFailsWith<IllegalStateException> { JchuConfig { install(failing) } }
        assertTrue("'boom'" in error.message.orEmpty())
    }

    @Test
    fun configOnceRunsTheBlockOnlyOnceAndReturnsTheSameResult() {
        resetJchuConfigOnce()
        val log = mutableListOf<String>()

        val first = JchuConfigOnce { install(Recorder("one", log = log)); koin() }
        val second = JchuConfigOnce { install(Recorder("two", log = log)) }

        assertEquals(listOf("one"), log)
        assertTrue(first === second)
        assertTrue(first.koin != null)
        resetJchuConfigOnce()
    }

    @Test
    fun resultHasNoKoinWithoutAKoinBlock() {
        assertNull(JchuConfig { install(Recorder("x", log = mutableListOf())) }.koin)
    }
}
