package com.github.lexer.injektor

import kotlin.reflect.KClass

internal open class KontainerImpl : Kontainer {
    private val bindings: HashMap<KClass<*>, Provider<*>> = HashMap()

    override fun <T : Any> get(clazz: KClass<T>): T {
        val result = bindings[clazz] as Provider<T>
        return result.provide()
    }

    override fun bindings(): Map<KClass<*>, Provider<*>> {
        return bindings
    }

    override fun <T : Any> bind(clazz: KClass<T>, provider: Provider<T>) {
        bindings[clazz] = provider
    }

    internal fun plus(modules: List<Module>) {
        modules.forEach {
                it.initialize(this)
        }
    }
}