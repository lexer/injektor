package com.github.lexer.injektor.coffee

import com.github.lexer.injektor.Kontainer
import com.github.lexer.injektor.Module

class CoffeeMakerModule : Module() {

    override fun configure(k: Kontainer) {
        bind<Pump> { Thermosiphon(heater = k.get(), logger = k.get()) }
        bind { CoffeeMaker(heater = k.get(), pump = k.get(), logger = k.get()) }
    }
}