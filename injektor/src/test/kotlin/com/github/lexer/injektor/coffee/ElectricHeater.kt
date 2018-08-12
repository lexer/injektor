package com.github.lexer.injektor.coffee

class ElectricHeater(val logger: Logger) : Heater {
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