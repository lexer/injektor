package com.github.lexer.injektor.coffee

class Thermosiphon(val heater: Heater, val logger: Logger) : Pump {
    init {
        logger.log("pump created")
    }

    override fun pump() {
        if (heater.isHot()) {
            logger.log("pump is pumping")
        }
    }
}