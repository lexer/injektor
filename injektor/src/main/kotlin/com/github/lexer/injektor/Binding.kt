package com.github.lexer.injektor

import kotlin.reflect.KClass

class Binding<T : Any>(val clazz: KClass<T>, val factory: () -> T) {
    override fun hashCode(): Int {
        return clazz.hashCode()
    }
}