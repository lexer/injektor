package com.github.lexer.injektor.sample

import android.app.Application
import com.github.lexer.injektor.Kontainer
import com.github.lexer.injektor.Module

class AppModule(val app: App) : Module() {

    override fun configure(k: Kontainer) {
        bind<Application> { app }
    }

}