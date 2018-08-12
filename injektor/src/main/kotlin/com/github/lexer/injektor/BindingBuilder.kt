package com.github.lexer.injektor

import kotlin.reflect.KClass

class BindingBuilder<T : Any>(val intf: KClass<T>, val factory: () -> T) {
    private var singleton = false

    fun asSingleton() {
        singleton = true
    }

    private fun provider(): Provider<T> {
        if (singleton) {
            return MemoizedProvider(factory)
        } else {
            return SimpleProvider(factory)
        }
    }

    internal fun bind(kontainer: Kontainer) {
        kontainer.bind(intf, provider())
    }
}