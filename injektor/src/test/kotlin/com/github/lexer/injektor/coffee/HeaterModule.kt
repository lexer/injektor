package com.github.lexer.injektor.coffee

import com.github.lexer.injektor.Injector
import com.github.lexer.injektor.Module

class HeaterModule : Module() {
    override fun configure(injector: Injector) {
        bind<Heater> { ElectricHeater(injector) }.scope("coffee")
    }
}