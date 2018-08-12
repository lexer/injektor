package com.github.lexer.injektor

import kotlin.reflect.KClass

interface Injector {

    fun <T : Any> get(clazz: KClass<T>): T

    fun bindings(): Map<KClass<*>, Provider<*>>

    fun <T : Any> bind(clazz: KClass<T>, provider: Provider<T>)

    companion object Builder {
        fun create(modules: List<Module>, logger: InjectorLogger = NoOpLogger()): Injector {
            val kontainer = InjectorImpl(logger)
            kontainer.plus(modules)
            return kontainer
        }
    }

    fun startScope(scopeName: String)
    fun stopScope(scopeName: String)
}

inline fun <reified T : Any> Injector.get(): T {
    return this.get(T::class)
}