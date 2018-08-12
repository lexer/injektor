package com.github.lexer.injektor

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class InjectorTest {
    private lateinit var injector: InjectorImpl
    private lateinit var mockLogger: MockLogger

    @Before
    fun setUp() {
        mockLogger = MockLogger()
        injector = InjectorImpl(mockLogger)
    }

    @Test
    fun bind_simpleProvider() {
        injector.bind(Class1::class, SimpleProvider { Class1() })
        val stubClass1 = injector.get(Class1::class)
        val stubClass2 = injector.get(Class1::class)

        assertThat(stubClass1).isNotNull()
        assertThat(stubClass2).isNotNull()
        assertThat(stubClass1).isNotEqualTo(stubClass2)
    }

    @Test
    fun plus_resolveInterfaceFromPlussedModule() {
        injector.plus(listOf(BindInterfaceToClassModule()))
        val stubClass1 = injector.get(Class1Interface::class)
        val stubClass2 = injector.get(Class1Interface::class)

        assertThat(stubClass1).isNotNull()
        assertThat(stubClass2).isNotNull()
        assertThat(stubClass1).isNotEqualTo(stubClass2)
    }

    @Test
    fun plus_resolveClassFromPlussedModule() {
        injector.plus(listOf(BindClassToClassModule()))
        val stubClass1 = injector.get(Class1::class)
        val stubClass2 = injector.get(Class1::class)

        assertThat(stubClass1).isNotNull()
        assertThat(stubClass2).isNotNull()
        assertThat(stubClass1).isNotEqualTo(stubClass2)
    }

    @Test
    fun plus_resolveClassFromScopedModule_sameInstance() {
        injector.plus(listOf(BindScopedModule()))

        injector.startScope("app")
        val stubClass1 = injector.get(Class1::class)
        val stubClass2 = injector.get(Class1::class)
        injector.stopScope("app")

        assertThat(stubClass1).isNotNull()
        assertThat(stubClass2).isNotNull()
        assertThat(stubClass1).isEqualTo(stubClass2)
    }

    @Test
    fun plus_resolveClassFromScopedModuleAndScopeRestarted_differentInstances() {
        injector.plus(listOf(BindScopedModule()))

        injector.startScope("app")
        val stubClass1 = injector.get(Class1::class)
        injector.stopScope("app")

        injector.startScope("app")
        val stubClass2 = injector.get(Class1::class)
        injector.stopScope("app")

        assertThat(stubClass1).isNotNull()
        assertThat(stubClass2).isNotNull()
        assertThat(stubClass1).isNotEqualTo(stubClass2)
    }

    @Test
    fun plus_resolveClassFromScopedModule_scopeNotStarted() {
        injector.plus(listOf(BindScopedModule()))
        val stubClass1 = injector.get(Class1::class)

        assertThat(stubClass1).isNotNull()
        val log = mockLogger.getLogs().get(0)
        assertThat(log.type).isEqualTo(InjectorLogger.MessageType.WARNING)
        assertThat(log.message)
                .isEqualTo("Scoped instance com.github.lexer.injektor.InjectorTest\$Class1 accessed before scope started \"app\"")
    }

    @Test
    fun plus_resolveClassFromScopedModule_scopeWasStopped() {
        injector.plus(listOf(BindScopedModule()))
        injector.startScope("app")
        injector.stopScope("app")
        val stubClass1 = injector.get(Class1::class)

        assertThat(stubClass1).isNotNull()
        val log = mockLogger.getLogs().get(0)
        assertThat(log.type).isEqualTo(InjectorLogger.MessageType.WARNING)
        assertThat(log.message)
                .isEqualTo("Scoped instance com.github.lexer.injektor.InjectorTest\$Class1 accessed before scope started \"app\"")
    }

    @Test
    fun stopScope_scopedClass_scopedInstanceDestroyed() {
        injector.plus(listOf(BindScopedModule()))

        injector.startScope("app")
        val stubClass1 = injector.get(Class1::class)
        injector.stopScope("app")

        assertThat(stubClass1).isNotNull()
        assertThat(stubClass1.isDestroyed).isEqualTo(true)
    }

    class BindInterfaceToClassModule : Module() {
        override fun configure(injector: Injector) {
            bind<Class1Interface> { Class1() }
        }
    }

    class BindClassToClassModule : Module() {
        override fun configure(injector: Injector) {
            bind { Class1() }
        }
    }

    class BindScopedModule : Module() {
        override fun configure(injector: Injector) {
            bind { Class1() }.scope("app")
        }
    }

    interface Class1Interface
    class Class1 : Class1Interface, Scoped {
        var isDestroyed: Boolean = false

        override fun onScopeDestroyed() {
            isDestroyed = true
        }

    }
}