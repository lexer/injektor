package com.github.lexer.injektor

import kotlin.reflect.KClass

interface Injector {

    fun <T : Any> get(clazz: KClass<T>): T

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