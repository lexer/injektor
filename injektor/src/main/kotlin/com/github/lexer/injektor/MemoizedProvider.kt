package com.github.lexer.injektor

internal class MemoizedProvider<T : Any>(initializer: () -> T) : Provider<T> {
    val value: T by lazy { initializer.invoke() }

    override fun provide(): T {
        return value
    }
}