package com.github.lexer.injektor.sample

import android.app.Application

class FooImpl(val app: Application) : Foo {

    override fun doSmth(): String {
        return app.javaClass.toString()
    }
}