package com.github.lexer.injektor

internal class SimpleProvider<T>(val factory: () -> T) : Provider<T> {
    override fun provide(): T {
        return factory.invoke()
    }
}