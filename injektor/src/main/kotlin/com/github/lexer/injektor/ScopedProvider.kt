package com.github.lexer.injektor

import java.lang.RuntimeException
import kotlin.reflect.KClass

internal class ScopedProvider<T : Any>(val scopeName: String, val klass: KClass<T>, val initializer: () -> T) : Provider<T> {
    private var scope: Scope? = null;

    override fun provide(): T {
        val cachedScope = scope;
        if (cachedScope != null) {
            return cachedScope.getOrCreateInstance(klass, initializer);
        } else {
            throw RuntimeException("Trying to resolve ${klass.java.name}, but scope \"${scopeName}\" was not found");
        }
    }

    fun setScope(scope: Scope) {
        this.scope = scope
    }
}