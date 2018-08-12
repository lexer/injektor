package com.github.lexer.injektor.coffee

import com.github.lexer.injektor.Kontainer
import com.github.lexer.injektor.Module

class HeaterModule : Module() {
    override fun dependencies(): List<Module> {
        return listOf(LoggerModule())
    }

    override fun configure(kontainer: Kontainer) {
        bind<Heater> { ElectricHeater(kontainer) }.asSingleton()
    }
}