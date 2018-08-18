package com.github.lexer.injektor

interface Injectable {
    fun kontainer(): Kontainer
}

inline fun <reified T : Any> Injectable.inject(): Lazy<T> = lazy {
    this.kontainer().get(T::class)
}