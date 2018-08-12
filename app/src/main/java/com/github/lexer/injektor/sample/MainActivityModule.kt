package com.github.lexer.injektor.sample

import android.app.Application
import com.github.lexer.injektor.Module

class MainActivityModule : Module() {
    val application: Application by resolve()

    override fun configure() {
        bind<Foo> { FooImpl(application) }
    }
}


