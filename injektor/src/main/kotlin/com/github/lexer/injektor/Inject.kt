package com.github.lexer.injektor

import kotlin.reflect.KProperty

interface Inject<out T> {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T

    fun isInitialized(): Boolean
}

fun <T> inject(initializer: () -> T): Inject<T> = SynchronizedInjectImpl(initializer)

internal object UNINITIALIZED_VALUE

var EAGER_INJECTION = false

private class SynchronizedInjectImpl<out T>(initializer: () -> T) : Inject<T> {
    private var initializer: (() -> T)? = initializer
    @Volatile
    private var _value: Any? = UNINITIALIZED_VALUE
    // final field is required to enable safe publication of constructed instance
    private val lock = this

    init {
        if (EAGER_INJECTION) {
            _value = initializer()
            this.initializer = null
        }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val value1 = _value
        if (value1 !== UNINITIALIZED_VALUE) {
            return value1 as T
        }

        return synchronized(lock) {
            val value2 = _value
            if (value2 !== UNINITIALIZED_VALUE) {
                value2 as T
            } else {
                val typedValue = initializer!!()
                _value = typedValue
                initializer = null
                typedValue
            }
        }
    }

    override fun isInitialized(): Boolean = _value !== UNINITIALIZED_VALUE
}