package com.github.lexer.injektor.coffee

import com.github.lexer.injektor.Injected
import com.github.lexer.injektor.Kontainer
import com.github.lexer.injektor.inject

class Thermosiphon(kontainer: Kontainer) : Injected(kontainer), Pump {
    val heater: Heater by inject()
    val logger: Logger by inject()

    init {
        logger.log("pump created")
    }

    override fun pump() {
        if (heater.isHot()) {
            logger.log("pump is pumping")
        }
    }
}