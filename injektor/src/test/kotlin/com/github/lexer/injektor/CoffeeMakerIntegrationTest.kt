package com.github.lexer.injektor

import org.junit.Test
import kotlin.collections.ArrayList
import kotlin.test.assertEquals

class CoffeeMakerIntegrationTest {

    @Test
    fun runCoffeeApp() {
        val logger = CoffeeApp().run()

        assertEquals(logger.popFirst(), "logger created")
        assertEquals(logger.popFirst(), "heater created")
        assertEquals(logger.popFirst(), "pump created")
        assertEquals(logger.popFirst(), "coffee maker created")
        assertEquals(logger.popFirst(), "heater is on")
        assertEquals(logger.popFirst(), "pump is pumping")
        assertEquals(logger.popFirst(), "coffee is brewed")
        assertEquals(logger.popFirst(), "heater is off")
    }


    class CoffeeApp : Injectable {
        val logger: Logger by inject()
        val cofferMaker: CoffeeMaker by inject()

        lateinit var coffeeContainer: Kontainer

        fun run(): Logger {
            val rootContainer = Kontainer.create(modules = LoggerModule())
            coffeeContainer = Kontainer.create(rootContainer, CoffeeMakerModule(), HeaterModule())

            cofferMaker.brew()

            return logger
        }

        override fun kontainer(): Kontainer {
            return coffeeContainer
        }
    }

    class HeaterModule : Module() {
        override fun configure() {
            bind<Heater> { ElectricHeater(logger = resolve()) }.asSingleton()
        }
    }

    class CoffeeMakerModule : Module() {

        override fun configure() {
            bind<Pump> { Thermosiphon(heater = resolve(), logger = resolve()) }
            bind { CoffeeMaker(heater = resolve(), pump = resolve(), logger = resolve()) }
        }
    }

    class LoggerModule : Module() {
        override fun configure() {
            bind { Logger() }.asSingleton()
        }
    }

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

    interface Heater {
        fun on()
        fun off()
        fun isHot(): Boolean
    }

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

    interface Pump {
        fun pump()
    }

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

    class Logger {
        val logs = ArrayList<String>()

        init {
            log("logger created")
        }

        fun log(message: String) {
            logs.add(message)
        }

        fun popFirst(): String {
            val first = logs.first()
            logs.remove(first)
            return first
        }
    }
}
