package com.github.lexer.injektor

import kotlin.reflect.KClass

class MockContainer : Kontainer {
    private val fakes : HashMap<KClass<*>, Any> = HashMap()

    override fun bindings(): Map<KClass<*>, Provider<*>> {
       return HashMap()
    }

    override fun <T : Any> bind(clazz: KClass<T>, provider: Provider<T>) {
    }


    override fun <T : Any> get(clazz: KClass<T>): T {
       return fakes[clazz] as T
    }

    fun <T : Any> mock(clazz : KClass<T> , mock : T) : MockContainer {
        fakes.put(clazz, mock)
        return this
    }

    inline fun <reified T : Any> mock(mock : T) : MockContainer {
        return mock(T::class, mock)
    }
}