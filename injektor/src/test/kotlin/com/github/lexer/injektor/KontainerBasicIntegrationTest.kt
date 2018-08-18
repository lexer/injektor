package com.github.lexer.injektor

import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

class KontainerBasicIntegrationTest {
    private lateinit var kontainer: KontainerImpl

    @Before
    fun setUp() {
        kontainer = KontainerImpl()
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
    fun bind_memoizedProvider() {
        kontainer.bind(Class1::class, MemoizedProvider { Class1() })
        val stubClass1 = kontainer.get(Class1::class)
        val stubClass2 = kontainer.get(Class1::class)

        assertNotNull(stubClass1)
        assertNotNull(stubClass2)
        assertEquals(stubClass1, stubClass2)
    }

    @Test
    fun extend() {
        val parentKotainer = KontainerImpl()
        parentKotainer.bind(Class1::class, SimpleProvider { Class1() })
        kontainer.extend(parentKotainer)
        val stubClass = kontainer.get(Class1::class)
        assertNotNull(stubClass)
    }

    @Test
    fun plus_resolveInterfaceFromPlussedModule() {
        kontainer.plus(arrayOf(BindInterfaceToClassModule()))
        val stubClass1 = kontainer.get(Class1Interface::class)
        val stubClass2 = kontainer.get(Class1Interface::class)

        assertNotNull(stubClass1)
        assertNotNull(stubClass2)
        assertNotEquals(stubClass1, stubClass2)
    }

    @Test
    fun plus_resolveClassFromPlussedModule() {
        kontainer.plus(arrayOf(BindClassToClassModule()))
        val stubClass1 = kontainer.get(Class1::class)
        val stubClass2 = kontainer.get(Class1::class)

        assertNotNull(stubClass1)
        assertNotNull(stubClass2)
        assertNotEquals(stubClass1, stubClass2)
    }

    @Test
    fun plus_resolveClassFromSingletondModule() {
        kontainer.plus(arrayOf(BindSingletonsModule()))
        val stubClass1 = kontainer.get(Class1::class)
        val stubClass2 = kontainer.get(Class1::class)

        assertNotNull(stubClass1)
        assertNotNull(stubClass2)
        assertEquals(stubClass1, stubClass2)
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

    class BindSingletonsModule : Module() {
        override fun configure(kontainer: Kontainer) {
            bind { Class1() }.asSingleton()
        }
    }

    interface Class1Interface
    class Class1 : Class1Interface
}