package com.github.lexer.injektor.coffee

import com.github.lexer.injektor.Injectable
import com.github.lexer.injektor.Injector
import com.github.lexer.injektor.inject

class Thermosiphon(override val injector: Injector) : Injectable, Pump {
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