package com.github.lexer.injektor.coffee

import com.github.lexer.injektor.Injector
import com.github.lexer.injektor.Module

class LoggerModule : Module() {
    override fun configure(injector: Injector) {
        bind { Logger() }.scope("app")
    }
}