package com.github.lexer.injektor

import kotlin.reflect.KClass

class MockInjector : Injector {
    override fun startScope(scopeName: String) {

    }

    override fun stopScope(scopeName: String) {

    }

    private val fakes: HashMap<KClass<*>, Any> = HashMap()

    override fun bindings(): Map<KClass<*>, Provider<*>> {
        return HashMap()
    }

    override fun <T : Any> bind(clazz: KClass<T>, provider: Provider<T>) {
    }


    override fun <T : Any> get(clazz: KClass<T>): T {
        return fakes[clazz] as T
    }

    fun <T : Any> mock(clazz: KClass<T>, mock: T): MockInjector {
        fakes.put(clazz, mock)
        return this
    }

    inline fun <reified T : Any> mock(mock: T): MockInjector {
        return mock(T::class, mock)
    }
}