package com.github.lexer.injektor

import kotlin.reflect.KClass

class BindingBuilder<T : Any>(val clazz: KClass<T>, val factory: () -> T) {
    private var scopeName: String? = null

    fun scope(scopeName: String) {
        this.scopeName = scopeName
    }

    private fun provider(): Provider<T> {
        val cacheScopeName = scopeName
        if (cacheScopeName != null) {
            return ScopedProvider(cacheScopeName, clazz, factory)
        } else {
            return SimpleProvider(factory)
        }
    }

    internal fun bind(injector: Injector) {
        injector.bind(clazz, provider())
    }
}