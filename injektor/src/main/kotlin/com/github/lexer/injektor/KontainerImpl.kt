package com.github.lexer.injektor

import kotlin.reflect.KClass

internal open class KontainerImpl(private val logger: InjektorLogger) : Kontainer {
    private val bindings: HashMap<KClass<*>, Provider<*>> = HashMap()
    private val scopes: HashMap<String, Scope> = HashMap()


    override fun startScope(scopeName: String) {
        val scope = getScope(scopeName)

        if (scope.isStarted()) {
            throw IllegalArgumentException("Scope \"$scopeName\" have been already started")
        } else {
            scope.start()
        }
    }

    override fun stopScope(scopeName: String) {
        val scope = getScope(scopeName)

        if (scope.isStarted()) {
            scope.stop()
        } else {
            throw IllegalArgumentException("Scope \"$scopeName\" haven't been started")
        }
    }

    override fun <T : Any> get(clazz: KClass<T>): T {
        val provider = bindings[clazz] as Provider<T>
        return provider.provide()
    }

    override fun bindings(): Map<KClass<*>, Provider<*>> {
        return bindings
    }

    override fun <T : Any> bind(clazz: KClass<T>, provider: Provider<T>) {
        if (provider is ScopedProvider) {
            val scope = getScope(provider.scopeName)
            provider.setScope(scope)
        }
        bindings[clazz] = provider
    }

    internal fun getScope(scopeName: String): Scope {
        return scopes.getOrPut(scopeName, { Scope(scopeName, logger) })
    }

    internal fun plus(modules: List<Module>) {
        modules.forEach {
            it.initialize(this)
        }
    }
}