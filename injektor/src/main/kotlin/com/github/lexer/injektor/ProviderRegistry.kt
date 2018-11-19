package com.github.lexer.injektor

import kotlin.reflect.KClass

interface ProviderRegistry {
    fun providers(): Map<KClass<*>, Provider<*>>
}
