package com.github.lexer.injektor.sample

import android.app.Application
import com.github.lexer.injektor.Kontainer
import com.github.lexer.injektor.Module

class MainActivityModule : Module() {

    override fun configure(k: Kontainer) {
        bind<Foo> { FooImpl(k.get()) }
    }
}


