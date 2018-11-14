package com.github.lexer.injektor

import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

class KontainerBasicIntegrationTest {
    private lateinit var kontainer: KontainerImpl
    private lateinit var mockLogger: MockLogger

    @Before
    fun setUp() {
        mockLogger = MockLogger()
        kontainer = KontainerImpl(mockLogger)
    }

    @Test
    fun bind_simpleProvider() {
        kontainer.bind(Class1::class, SimpleProvider { Class1() })
        val stubClass1 = kontainer.get(Class1::class)
        val stubClass2 = kontainer.get(Class1::class)

        assertNotNull(stubClass1)
        assertNotNull(stubClass2)
        assertNotEquals(stubClass1, stubClass2)
    }

    @Test
    fun plus_resolveInterfaceFromPlussedModule() {
        kontainer.plus(listOf(BindInterfaceToClassModule()))
        val stubClass1 = kontainer.get(Class1Interface::class)
        val stubClass2 = kontainer.get(Class1Interface::class)

        assertNotNull(stubClass1)
        assertNotNull(stubClass2)
        assertNotEquals(stubClass1, stubClass2)
    }

    @Test
    fun plus_resolveClassFromPlussedModule() {
        kontainer.plus(listOf(BindClassToClassModule()))
        val stubClass1 = kontainer.get(Class1::class)
        val stubClass2 = kontainer.get(Class1::class)

        assertNotNull(stubClass1)
        assertNotNull(stubClass2)
        assertNotEquals(stubClass1, stubClass2)
    }

    @Test
    fun plus_resolveClassFromScopedModule_sameScope() {
        kontainer.plus(listOf(BindScopedModule()))

        kontainer.startScope("app");
        val stubClass1 = kontainer.get(Class1::class)
        val stubClass2 = kontainer.get(Class1::class)
        kontainer.stopScope("app");

        assertNotNull(stubClass1)
        assertNotNull(stubClass2)
        assertEquals(stubClass1, stubClass2)
    }

    @Test
    fun plus_resolveClassFromScopedModule_scopeRestarted() {
        kontainer.plus(listOf(BindScopedModule()))

        kontainer.startScope("app");
        val stubClass1 = kontainer.get(Class1::class)
        kontainer.stopScope("app");

        kontainer.startScope("app");
        val stubClass2 = kontainer.get(Class1::class)
        kontainer.stopScope("app");

        assertNotNull(stubClass1)
        assertNotNull(stubClass2)
        assertNotEquals(stubClass1, stubClass2)
    }

    @Test
    fun plus_resolveClassFromScopedModule_scopeNotStarted() {
        kontainer.plus(listOf(BindScopedModule()))
        val stubClass1 = kontainer.get(Class1::class)

        assertNotNull(stubClass1)
        val log = mockLogger.getLogs().get(0)
        assertEquals(log.type, InjektorLogger.MessageType.WARNING)
        assertEquals(log.message, "Scoped instance com.github.lexer.injektor.KontainerBasicIntegrationTest\$Class1 accessed before scope started \"app\"")
    }

    @Test
    fun plus_resolveClassFromScopedModule_scopeWasStopped() {
        kontainer.plus(listOf(BindScopedModule()))
        kontainer.startScope("app");
        kontainer.stopScope("app");
        val stubClass1 = kontainer.get(Class1::class)

        assertNotNull(stubClass1)
        val log = mockLogger.getLogs().get(0)
        assertEquals(log.type, InjektorLogger.MessageType.WARNING)
        assertEquals(log.message, "Scoped instance com.github.lexer.injektor.KontainerBasicIntegrationTest\$Class1 accessed before scope started \"app\"")
    }

    class BindInterfaceToClassModule : Module() {
        override fun configure(kontainer: Kontainer) {
            bind<Class1Interface> { Class1() }
        }
    }

    class BindClassToClassModule : Module() {
        override fun configure(kontainer: Kontainer) {
            bind { Class1() }
        }
    }

    class BindScopedModule : Module() {
        override fun configure(kontainer: Kontainer) {
            bind { Class1() }.scope("app")
        }
    }

    interface Class1Interface
    class Class1 : Class1Interface
}