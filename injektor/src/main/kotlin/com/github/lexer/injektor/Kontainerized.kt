package com.github.lexer.injektor

open class Kontainerized(val kontainer: Kontainer) : Injectable {
    override fun kontainer(): Kontainer {
        return kontainer
    }
}