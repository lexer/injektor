package com.github.lexer.injektor.coffee

import com.github.lexer.injektor.Module

class HeaterModule : Module() {
    override fun configure() {
        bind<Heater> { ElectricHeater(logger = resolve()) }.asSingleton()
    }
}