package com.github.lexer.injektor

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class UnitTestingExample {
    @Mock
    lateinit var dependency : Dependency

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun mooTest() {
        val mockContainer = MockContainer()
                .mock(dependency)
        val testedClass = CowClass(mockContainer)

        testedClass.sayMoo()

        Mockito.verify(dependency).moo()
    }

    class CowClass(kontainer: Kontainer) : Kontainerized(kontainer) {
        private val dependency : Dependency by inject()

        fun sayMoo() : String {
            return dependency.moo()
        }
    }

    interface Dependency {
        fun moo() : String
    }
}
