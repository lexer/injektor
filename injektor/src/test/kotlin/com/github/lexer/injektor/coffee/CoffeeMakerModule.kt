package com.github.lexer.injektor.coffee

import com.github.lexer.injektor.Module

class CoffeeMakerModule : Module() {

    override fun configure() {
        bind<Pump> { Thermosiphon(heater = resolve(), logger = resolve()) }
        bind { CoffeeMaker(heater = resolve(), pump = resolve(), logger = resolve()) }
    }
}