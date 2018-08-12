package com.github.lexer.injektor

import kotlin.collections.ArrayList
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

abstract class Module {
    private lateinit var kontainer: Kontainer
    private val bindings = ArrayList<BindingBuilder<*>>()

    fun <T : Any> resolve(klass: KClass<T>): T {
        return kontainer.get(klass)
    }

    inline fun <reified T : Any> resolve(): ReadOnlyProperty<Module, T> = object : ReadOnlyProperty<Module, T> {
        override fun getValue(thisRef: Module, property: KProperty<*>): T {
            return resolve(T::class)
        }
    }

    fun <T : Any> bind(klass: KClass<T>, factory: () -> T): BindingBuilder<T> {
        val binding = BindingBuilder(klass, factory)
        bindings.add(binding)
        return binding
    }

    inline fun <reified T : Any> bind(noinline factory: () -> T): BindingBuilder<T> {
        return bind(T::class, factory)
    }

    abstract fun configure()

    internal fun initialize(kontainer: Kontainer) {
        this.kontainer = kontainer
        configure()
        for (binding in bindings) {
            binding.bind(kontainer)
        }
    }
}
