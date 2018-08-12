package com.github.lexer.injektor.coffee

class CoffeeMaker(val heater: Heater, val pump: Pump, val logger: Logger) {
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