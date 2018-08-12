package com.github.lexer.injektor.coffee

import com.github.lexer.injektor.Injector
import com.github.lexer.injektor.Module

class CoffeeMakerModule : Module() {
    override fun configure(injector: Injector) {
        bind<Pump> { Thermosiphon(injector) }
        bind { CoffeeMaker(injector) }
    }
}