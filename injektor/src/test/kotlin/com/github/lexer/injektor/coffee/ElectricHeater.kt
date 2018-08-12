package com.github.lexer.injektor.coffee

import com.github.lexer.injektor.Injectable
import com.github.lexer.injektor.Injector
import com.github.lexer.injektor.inject

class ElectricHeater(override val injector: Injector) : Injectable, Heater {
    private val logger: Logger by inject()

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