package com.github.lexer.injektor.coffee

import com.github.lexer.injektor.Module

class LoggerModule : Module() {
    override fun configure() {
        bind { Logger() }.asSingleton()
    }
}