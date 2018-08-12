package com.github.lexer.injektor

interface Provider<T> {
    fun provide(): T
}