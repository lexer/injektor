package com.github.lexer.injektor.coffee

import com.github.lexer.injektor.Injectable
import com.github.lexer.injektor.Injector
import com.github.lexer.injektor.inject

class CoffeeMaker(override val injector: Injector) : Injectable {

    private val heater: Heater by inject()
    private val pump: Pump by inject()
    private val logger: Logger by inject()

    init {
        logger.log("coffee maker created")
    }

    fun brew() {
        heater.on()
        pump.pump()
        logger.log("coffee is brewed")
        heater.off()
    }
}