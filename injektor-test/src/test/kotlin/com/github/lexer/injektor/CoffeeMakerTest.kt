package com.github.lexer.injektor

import com.github.lexer.injektor.coffee.CoffeeMaker
import com.github.lexer.injektor.coffee.Heater
import com.github.lexer.injektor.coffee.Logger
import com.github.lexer.injektor.coffee.Pump
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class CoffeeMakerTest {
    @Mock lateinit var pump: Pump
    @Mock lateinit var heater: Heater
    private lateinit var coffeeMaker: CoffeeMaker

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        coffeeMaker = CoffeeMaker(MockInjector()
                .mock(pump)
                .mock(heater)
                .mock(Logger()))
    }

    @Test
    fun brew() {
        coffeeMaker.brew()

        verify(heater).on()
        verify(pump).pump()
        verify(heater).off()
    }
}
