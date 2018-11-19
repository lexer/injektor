package com.github.lexer.injektor

import kotlin.reflect.KClass

abstract class Module {
    private lateinit var injector: Injector
    private val bindings = ArrayList<Binding<*>>()

    fun <T : Any> bind(klass: KClass<T>, factory: () -> T): Binding<T> {
        val binding = Binding(klass, factory)
        bindings.add(binding)
        return binding
    }

    inline fun <reified T : Any> bind(noinline factory: () -> T): Binding<T> {
        return bind(T::class, factory)
    }

    abstract fun configure(injector: Injector)

    internal fun initialize(injector: Injector) {
        this.injector = injector
        configure(injector)
        for (binding in bindings) {
            binding.bind(injector)
        }
    }
}
