package com.github.lexer.injektor

interface Injectable {
    val injector: Injector
}

inline fun <reified T : Any> Injectable.inject(): Inject<T> = inject {
    this.injector.get(T::class)
}