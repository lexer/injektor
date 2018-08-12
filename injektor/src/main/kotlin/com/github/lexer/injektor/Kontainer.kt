package com.github.lexer.injektor

import kotlin.reflect.KClass
import kotlin.collections.HashMap

class Kontainer internal constructor() {

    private val hashMap: HashMap<KClass<*>, Provider<*>> = HashMap()

    fun <T : Any> get(clazz: KClass<T>): T {
        val result = hashMap[clazz] as Provider<T>
        return result.provide()
    }

    inline fun <reified T : Any> get(): T {
        return this.get(T::class)
    }

    internal fun <T : Any> bind(clazz: KClass<T>, provider: Provider<T>) {
        hashMap[clazz] = provider
    }

    internal fun extend(parent: Kontainer) {
        hashMap.putAll(parent.hashMap)
    }

    internal fun plus(modules: Array<out Module>) {
        modules.forEach {
            it.initialize(this)
            it.configure()
        }
    }

    companion object Factory {
        fun create(parent: Kontainer = Kontainer(), vararg modules: Module): Kontainer {
            val kontainer = Kontainer()
            kontainer.extend(parent)
            kontainer.plus(modules)
            return kontainer
        }
    }
}