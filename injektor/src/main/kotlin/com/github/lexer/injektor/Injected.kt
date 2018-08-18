package com.github.lexer.injektor

open class Injected(val kontainer: Kontainer) : Injectable {
    override fun kontainer(): Kontainer {
        return kontainer
    }
}