package com.github.lexer.injektor

import com.github.lexer.injektor.coffee.CoffeeApp
import org.junit.Test
import kotlin.test.assertEquals

class CoffeeMakerIntegrationTest {

    @Test
    fun runCoffeeApp() {
        val logger = CoffeeApp().run()

        assertEquals(logger.popFirst(), "logger created")
        assertEquals(logger.popFirst(), "heater created")
        assertEquals(logger.popFirst(), "pump created")
        assertEquals(logger.popFirst(), "coffee maker created")
        assertEquals(logger.popFirst(), "heater is on")
        assertEquals(logger.popFirst(), "pump is pumping")
        assertEquals(logger.popFirst(), "coffee is brewed")
        assertEquals(logger.popFirst(), "heater is off")
    }


}
