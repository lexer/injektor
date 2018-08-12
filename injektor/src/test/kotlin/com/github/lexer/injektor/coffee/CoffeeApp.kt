package com.github.lexer.injektor.coffee

import com.github.lexer.injektor.Injectable
import com.github.lexer.injektor.Injector
import com.github.lexer.injektor.inject

class CoffeeApp : Injectable {
    val logger: Logger by inject()
    val cofferMaker: CoffeeMaker by inject()

    override val injector: Injector
            by lazy { Injector.create(modules = listOf(LoggerModule(), HeaterModule(), CoffeeMakerModule())) }

    fun run(): Logger {
        cofferMaker.brew()

        return logger
    }
}