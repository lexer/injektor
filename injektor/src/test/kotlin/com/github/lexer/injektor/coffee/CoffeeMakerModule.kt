package com.github.lexer.injektor.coffee

import com.github.lexer.injektor.Kontainer
import com.github.lexer.injektor.Module

class CoffeeMakerModule : Module() {
    override fun dependencies(): List<Module> {
        return listOf(HeaterModule(), LoggerModule())
    }

    override fun configure(kontainer: Kontainer) {
        bind<Pump> { Thermosiphon(kontainer) }
        bind { CoffeeMaker(kontainer) }
    }
}