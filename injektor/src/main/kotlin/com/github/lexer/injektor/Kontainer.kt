package com.github.lexer.injektor

import kotlin.reflect.KClass

interface Kontainer {

    fun <T : Any> get(clazz: KClass<T>): T

    fun bindings(): Map<KClass<*>, Provider<*>>

    fun <T : Any> bind(clazz: KClass<T>, provider: Provider<T>)

    companion object Builder {
        fun create(parent: Kontainer? = null, modules: List<Module>): Kontainer {
            val kontainer = KontainerImpl()
            if (parent != null) {
                kontainer.extend(parent)
            }
            kontainer.plus(modules)
            return kontainer
        }
    }
}

inline fun <reified T : Any> Kontainer.get(): T {
    return this.get(T::class)
}