package com.github.lexer.injektor

import kotlin.reflect.KClass

internal open class KontainerImpl : Kontainer {
    private val bindings: HashMap<KClass<*>, Provider<*>> = HashMap()
    private val addedModules: HashSet<KClass<out Module>> = HashSet()

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

    internal fun extend(parent: Kontainer) {
        bindings.putAll(parent.bindings())
        addedModules.addAll((parent as KontainerImpl).addedModules);
    }

    internal fun plus(modules: List<Module>) {
        modules.forEach {
            if (!addedModules.contains(it::class)) {
                plus(it.dependencies())
                it.initialize(this)
                addedModules.add(it::class)
            }
        }
    }
}