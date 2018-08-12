package com.github.lexer.injektor

import kotlin.reflect.KClass

internal class Scope(private val name: String, private val logger: InjectorLogger) {
    private val cachedInstances: HashMap<KClass<*>, Any> = HashMap()
    private var started = false

    fun <T : Any> getOrCreateInstance(klass: KClass<T>, initializer: () -> T): T {
        if (!isStarted()) {
            logger.log(InjectorLogger.MessageType.WARNING, "Scoped instance ${klass.java.name} accessed before scope started \"${name}\"")
        }

        val instance = cachedInstances.get(klass)
        if (instance == null) {
            val newInstance = initializer.invoke()
            cachedInstances.put(klass, newInstance)
            return newInstance
        }
        return instance as T
    }

    fun start() {
        started = true
    }

    fun stop() {
        cachedInstances.forEach {
            val value = it.value
            if (value is Scoped) {
                value.onScopeDestroyed()
            }
        }
        cachedInstances.clear()
        started = false
    }

    fun isStarted(): Boolean {
        return started
    }
}