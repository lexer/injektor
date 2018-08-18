package com.github.lexer.injektor.coffee

import com.github.lexer.injektor.Injected
import com.github.lexer.injektor.Kontainer
import com.github.lexer.injektor.inject

class ElectricHeater(kontainer: Kontainer) : Injected(kontainer), Heater {
    val logger: Logger by inject()

    init {
        logger.log("heater created")
    }

    var isOn = false
    override fun on() {
        logger.log("heater is on")
        isOn = true
    }

    override fun off() {
        logger.log("heater is off")
        isOn = false
    }

    override fun isHot(): Boolean {
        return isOn
    }
}