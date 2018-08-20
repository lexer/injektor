package com.github.lexer.injektor.coffee

import com.github.lexer.injektor.Kontainerized
import com.github.lexer.injektor.Kontainer
import com.github.lexer.injektor.inject

class Thermosiphon(kontainer: Kontainer) : Kontainerized(kontainer), Pump {
    private val heater: Heater by inject()
    private val logger: Logger by inject()

    init {
        logger.log("pump created")
    }

    override fun pump() {
        if (heater.isHot()) {
            logger.log("pump is pumping")
        }
    }
}