package com.github.lexer.injektor.coffee

import com.github.lexer.injektor.Injected
import com.github.lexer.injektor.Kontainer
import com.github.lexer.injektor.inject

class CoffeeMaker(kontainer: Kontainer) : Injected(kontainer) {

    val heater: Heater by inject()
    val pump: Pump by inject()
    val logger: Logger by inject()

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