package com.github.lexer.injektor

interface Injectable {
    fun kontainer(): Kontainer
}

inline fun <reified T : Any> Injectable.inject(): Inject<T> = injected {
    this.kontainer().get(T::class)
}