package com.github.lexer.injektor.sample

import android.app.Application
import com.github.lexer.injektor.Kontainerized
import com.github.lexer.injektor.Kontainer
import com.github.lexer.injektor.inject

class FooImpl(kontainer: Kontainer) : Kontainerized(kontainer), Foo {
    val app: Application by inject()

    override fun doSmth(): String {
        return app.javaClass.toString()
    }
}