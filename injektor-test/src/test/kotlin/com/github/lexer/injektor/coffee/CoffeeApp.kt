package com.github.lexer.injektor.coffee

import com.github.lexer.injektor.Injectable
import com.github.lexer.injektor.Kontainer
import com.github.lexer.injektor.inject

class CoffeeApp : Injectable {
    val logger: Logger by inject()
    val cofferMaker: CoffeeMaker by inject()
    val kontainer = Kontainer.create(modules = listOf(LoggerModule(), HeaterModule(), CoffeeMakerModule()))

    fun run(): Logger {
        cofferMaker.brew()

        return logger
    }

    override fun kontainer(): Kontainer {
        return kontainer;
    }
}