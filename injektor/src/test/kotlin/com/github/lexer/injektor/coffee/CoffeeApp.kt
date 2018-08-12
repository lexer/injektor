package com.github.lexer.injektor.coffee

import com.github.lexer.injektor.Injectable
import com.github.lexer.injektor.Kontainer
import com.github.lexer.injektor.inject

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