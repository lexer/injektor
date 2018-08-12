package com.github.lexer.injektor.sample

import com.github.lexer.injektor.Injector
import com.github.lexer.injektor.Module

class AppModule(val app: App) : Module() {
    override fun configure(injector: Injector) {
        bind<App> { app }
    }

}