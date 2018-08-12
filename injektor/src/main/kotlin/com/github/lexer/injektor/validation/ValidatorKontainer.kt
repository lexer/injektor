package com.github.lexer.injektor.validation

import com.github.lexer.injektor.KontainerImpl

internal class ValidatorKontainer : KontainerImpl() {
    fun resolveAll(): List<ModuleValidationError> {
        val result = ArrayList<ModuleValidationError>()
        bindings().forEach {
            try {
                it.value.provide();
            } catch (e : Exception) {
                result.add(ModuleValidationError(it.key, e))
            }
        }
        return result
    }
}