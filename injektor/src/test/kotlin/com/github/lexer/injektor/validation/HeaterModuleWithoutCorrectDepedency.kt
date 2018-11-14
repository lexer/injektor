package com.github.lexer.injektor.validation

import com.github.lexer.injektor.Kontainer
import com.github.lexer.injektor.Module
import com.github.lexer.injektor.coffee.ElectricHeater
import com.github.lexer.injektor.coffee.Heater

class HeaterModuleWithoutCorrectDepedency : Module() {
    override fun configure(kontainer: Kontainer) {
        bind<Heater> { ElectricHeater(kontainer) }.scope("coffee")
    }
}