package com.github.lexer.injektor.sample

import android.app.Application
import com.github.lexer.injektor.Kontainer
import com.github.lexer.injektor.Module

class MainActivityModule : Module() {
    override fun dependencies(): List<Module> {
        return emptyList()
    }

    override fun configure(kontainer: Kontainer) {
        bind<Foo> { FooImpl(kontainer) }
    }
}


