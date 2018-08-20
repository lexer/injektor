package com.github.lexer.injektor.coffee

import com.github.lexer.injektor.Kontainerized
import com.github.lexer.injektor.Kontainer
import com.github.lexer.injektor.inject

class CoffeeMaker(kontainer: Kontainer) : Kontainerized(kontainer) {

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